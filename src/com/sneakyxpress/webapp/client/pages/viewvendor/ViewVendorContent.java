package com.sneakyxpress.webapp.client.pages.viewvendor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.sneakyxpress.webapp.client.customwidgets.ReviewWidget;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.facebook.Share;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.truckclaim.TruckClaimContent;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.shared.FoodVendor;
import com.sneakyxpress.webapp.shared.VendorFeedback;
import org.cobogw.gwt.user.client.ui.Rating;

import java.util.List;

/**
 * The UI of the Food Vendor pages.
 */
public class ViewVendorContent extends Content {
	private static final String pageName = "View Vendor";
	private static final String pageStub = "view";

	public static FoodVendor vendor;

	private final ViewVendorServiceAsync viewVendorService = GWT
			.create(ViewVendorService.class);

	public ViewVendorContent(Sneaky_Xpress module) {
		super(module);
	}

	@Override
	public String getPageName() {
		return pageName;
	}

	@Override
	public String getPageStub() {
		return pageStub;
	}

	@Override
	public void getAndChangeContent(String input) {
		viewVendorService.viewVendorServer(input,
				new AsyncCallback<FoodVendor>() {
			@Override
			public void onFailure(Throwable caught) {
				module.addMessage(true, GENERIC_ERROR_MESSAGE
						+ " Reason: " + caught.getMessage());
			}

			@Override
			public void onSuccess(FoodVendor vendor) {
                ViewVendorContent.vendor = vendor;

                // Mark this vendor as recently viewed
                module.FACEBOOK_TOOLS.addViewedVendor(vendor);

				HTMLPanel content = new HTMLPanel(""); // The base panel to hold all contents

				/*
				 * Contains all the information on the food vendor
				 */
				HTMLPanel info = new HTMLPanel("");
				info.addStyleName("row-fluid");

				// The basic text information
				String name = vendor.getName();
				if (name.isEmpty()) {
					name = "<em class=\"muted\">No Name Available</em>";
				}

				final HTMLPanel textInfo = new HTMLPanel(
						"<div class=\"page-header\"><h2>" + name
						+ "</h2></div>");
				textInfo.addStyleName("span6");
				
				// Facebook Like and Share
				textInfo.add(new Share(Window.Location.getHref()));

				textInfo.add(getInfoWidget("Description",
						vendor.getDescription()));
				textInfo.add(getInfoWidget("Location",
						vendor.getLocation()));

                // Claim vendor link
                HTMLPanel claim = new HTMLPanel("p", "<strong>Claim this Business</strong><br>"
                        + "Do you own this business? ");
                claim.add(getClaimLink());
                textInfo.add(claim);

				// Group all the information together
				HTMLPanel mapInfo = new HTMLPanel("");
				mapInfo.setStyleName("span6 map_canvas");
				mapInfo.getElement().setId("small_map_canvas");

				info.add(textInfo);
				info.add(mapInfo);

				content.add(info);

                /*
                 * The rating and reviews, also a form to add new reviews
                 */
                HTMLPanel reviews = new HTMLPanel("");
                reviews.addStyleName("row-fluid");
                reviews.getElement().setAttribute("style", "padding-top: 10px;");

                // The existing reviews
                final HTMLPanel showReviews = new HTMLPanel("<h3>Reviews</h3>");
                showReviews.addStyleName("span6");

                VendorFeedbackServiceAsync vendorFeedbackService = GWT.
                        create(VendorFeedbackService.class);
                vendorFeedbackService.getVendorReviews(vendor.getVendorId(), new AsyncCallback<List<VendorFeedback>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, "Error loading reviews. Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<VendorFeedback> result) {
                        if (result == null) {
                            HTMLPanel response = new HTMLPanel("<p class=\"lead pagination-centered\">" +
                                    "No reviews could be found :(</p>");
                            response.addStyleName("well");
                            showReviews.add(response);

                            textInfo.add(getInfoWidget("Average Rating", ""));
                        } else {
                            double mean = 0.0;
                            for (VendorFeedback v : result) {
                                showReviews.add(new ReviewWidget(module, v));
                                mean += v.getRating();
                            }
                            mean /= result.size();
                            String formatted = NumberFormat.getFormat("0.0").format(mean);
                            textInfo.add(getInfoWidget("Average Rating", formatted + " Stars"));
                        }
                    }
                });

                // Add a new review
                HTMLPanel addReview = new HTMLPanel("<h3>Add a Review</h3>");
                addReview.addStyleName("span6");

                if (module.FACEBOOK_TOOLS.isLoggedIn()) {
                    HTMLPanel form = new HTMLPanel("form", "");
                    HTMLPanel fieldSet = new HTMLPanel("fieldset", "<label>Rating</label>");

                    // The star rating
                    Rating rating = new Rating(0, 5);
                    fieldSet.add(rating);

                    // The text review
                    fieldSet.add(new HTML("<br><br>"));
                    fieldSet.add(new HTML("<label>Review (optional)</label>"));

                    TextArea reviewText = new TextArea();
                    reviewText.addStyleName("input-block-level");
                    fieldSet.add(reviewText);

                    // The submit button
                    fieldSet.add(new HTML("<br>"));

                    Button submitButton = new Button("Submit Review");
                    submitButton.addStyleName("btn btn-info");
                    submitButton.addClickHandler(new ReviewSubmitClickHandler(rating, reviewText));
                    fieldSet.add(submitButton);

                    form.add(fieldSet);
                    addReview.add(form);
                } else {
                    HTMLPanel response = new HTMLPanel("<p class=\"lead pagination-centered\">" +
                            "Please log in to post a review.</p>");
                    response.addStyleName("well");
                    addReview.add(response);
                }

                // Put the review divs together and add it to the contents
                reviews.add(showReviews);
                reviews.add(addReview);
                content.add(reviews);
				
				/*
				 * Change the content
				 */
				module.changeContent(content);


				/*
				 * A simple map (it must be last or else it doesn't really work)
				 */
				MapOptions options = MapOptions.create();
				options.setZoom(14.0);
				options.setCenter(LatLng.create(vendor.getLatitude(),
						vendor.getLongitude()));
				options.setMapTypeId(MapTypeId.ROADMAP);
				options.setDraggable(true);
				options.setMapTypeControl(true);
				options.setScaleControl(true);
				options.setScrollwheel(false);

				GoogleMap map = GoogleMap.create(Document.get()
						.getElementById("small_map_canvas"), options);

				MarkerOptions markerOptions = MarkerOptions.create();
				markerOptions.setPosition(LatLng.create(
						vendor.getLatitude(), vendor.getLongitude()));
				markerOptions.setMap(map);
				markerOptions.setTitle(vendor.getName());
				Marker.create(markerOptions);
			}

			private HTML getInfoWidget(String title, String info) {
				if (info.isEmpty()) {
					info = "<em class=\"muted\">Information currently not available</em>";
				}

				return new HTML("<p><strong>" + title + "</strong><br>"
						+ info + "</p>");
			}
		});
	}

	private Anchor getClaimLink() {
		// Truck owner claim button
        Anchor link = new Anchor("Claim it!");
		link.addClickHandler(new PageClickHandler(new TruckClaimContent(module), vendor.getVendorId()));
		return link;
	}

    private class ReviewSubmitClickHandler implements ClickHandler {
        private final Rating rating;
        private final TextArea text;
        private final FacebookTools facebook = module.FACEBOOK_TOOLS;

        public ReviewSubmitClickHandler(Rating rating, TextArea text) {
            this.rating = rating;
            this.text = text;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (verifyInput()) {
                final VendorFeedback feedback = new VendorFeedback();
                feedback.setAuthorId(facebook.getUserId());
                feedback.setAuthorName(facebook.getUserName());
                feedback.setVendorId(vendor.getVendorId());
                feedback.setVendorName(vendor.getName());
                feedback.setRating(rating.getValue());
                feedback.setReview(text.getText());

                VendorFeedbackServiceAsync vendorFeedbackService = GWT.
                        create(VendorFeedbackService.class);
                vendorFeedbackService.persistVendorFeedback(feedback, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, "Adding review failed. Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        module.addMessage(false, "Your review was successfully added. Thank you!");
                    }
                });
            }
        }

        private boolean verifyInput() {
            return true;
        }
    }
}
