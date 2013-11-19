package com.sneakyxpress.webapp.client.pages.viewvendor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.facebook.Share;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.truckclaim.TruckClaimContent;
import com.sneakyxpress.webapp.shared.FoodVendor;
import com.sneakyxpress.webapp.shared.VendorFeedback;

/**
 * The UI of the Food Vendor pages.
 */
public class ViewVendorContent extends Content {
	private static final String pageName = "View Vendor";
	private static final String pageStub = "view";

	public String vendorId = "";

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
                // Mark this vendor as recently viewed
                module.FACEBOOK_TOOLS.addViewedVendor(vendor);

				HTMLPanel content = new HTMLPanel(""); // The base panel
				// to hold all
				// content

				// Contains the basic information of the Food Vendor
				HTMLPanel info = new HTMLPanel("");
				info.addStyleName("row-fluid");

				vendorId = vendor.getVendorId();

				// The basic text information
				String name = vendor.getName();
				if (name.isEmpty()) {
					name = "<em class=\"muted\">No Name Available</em>";
				}

				HTMLPanel textInfo = new HTMLPanel(
						"<div class=\"page-header\"><h2>" + name
						+ "</h2></div>");
				textInfo.addStyleName("span6");
				
				// Facebook Like and Share
				textInfo.add(new Share(Window.Location.getHref()));
				
				textInfo.add(getInfoWidget("Rating",
						Integer.toString(vendor.getAverageRating())));
				textInfo.add(getInfoWidget("Description",
						vendor.getDescription()));
				textInfo.add(getInfoWidget("Location",
						vendor.getLocation()));
				if (vendor.getVendorFeedback().size() == 0) {
					textInfo.add(getInfoWidget("Reviews", "No reviews are currently available"));
				} else { textInfo.add(displayReviews(vendor)); }
				
				textInfo.add(new HTML("<br>")); // Some padding
				textInfo.add(new HTML("<p><strong>Submit a Review!</strong><br>"));
				textInfo.add(new HTML("<p>Rating<br>"));
				textInfo.add(new HTML("<input type=" + "rating" 
						+ " class=" + "form-control" + " id=" + "Rating" + " placeholder="
						+ "rating>"));
				textInfo.add(new HTML("<p>Review<br>"));
				textInfo.add(new HTML("<textarea class="+"form-control"+" rows="+"3"+"></textarea>"));
				textInfo.add(getSubmitReviewButton());
				
				textInfo.add(new HTML("<br>")); // Some padding
				textInfo.add(new HTML("<br>"));

				// Group all the information together
				HTMLPanel mapInfo = new HTMLPanel("");
				mapInfo.setStyleName("span6 map_canvas");
				mapInfo.getElement().setId("small_map_canvas");

				info.add(textInfo);
				info.add(mapInfo);

				content.add(info);
				
				content.add(getClaimButton());

				
				// Change the content
				module.changeContent(content);

				// A simple map (it must be last or else it doesn't
				// really work)
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
			
			private SimpleTable displayReviews(FoodVendor vendor) {
				SimpleTable reviewTable = new SimpleTable("table-hover table-bordered table-striped",
                        "User", "Review", "Rating", "Date");
					
				for (VendorFeedback vf : vendor.getVendorFeedback()) {
					reviewTable.addRow(vf.getAuthorUserId(), vf.getReview(), 
							Integer.toString(vf.getRating()), Long.toString(vf.getCreationTime()));
				}
				return reviewTable;
			}
			
		});
	}
	

	public Button getClaimButton() {
		// Truck owner claim button
		Button button = new Button("Claim Truck");
		button.addStyleName("btn btn-primary btn-sm");
		button.addClickHandler(new PageClickHandler(new TruckClaimContent(module), vendorId));		
		return button;
	}
	
	public Button getSubmitReviewButton() {
		Button button = new Button("Submit");
		button.addStyleName("btn btn-primary btn-sm");
		button.addClickHandler(new PageClickHandler(new ViewVendorContent(module), vendorId));		
		return button;
	}
	

}
