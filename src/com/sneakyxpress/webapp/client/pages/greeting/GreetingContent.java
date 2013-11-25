package com.sneakyxpress.webapp.client.pages.greeting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.customwidgets.FavouriteButton;
import com.sneakyxpress.webapp.client.customwidgets.ReviewWidget;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesServiceAsync;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.*;

/**
 * Get the content for our home page.
 */
public class GreetingContent extends Content {
	private static final String pageName = "Vancouver Food Vendor Reviews";
	private static final String pageStub = "home";

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public GreetingContent(Sneaky_Xpress module) {
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
		greetingService.greetServer(input, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				module.addMessage(true, GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
			}

			public void onSuccess(String result) {
				HTMLPanel parentContents = new HTMLPanel("");

				final HTMLPanel contents = new HTMLPanel("");
				contents.addStyleName("row-fluid");

				HTMLPanel info = null;
				if(!FacebookTools.isLoggedIn())
				{
					info = new HTMLPanel("<p class=\"lead pagination-centered\">Click on our Login button at the top to access your account activity or to register today with your Facebook account!</p>");
				}
				else{
					info = new HTMLPanel("<p class=\"lead pagination-centered\">Click on our Profile button at the top to access your account activity.</p>");
				}


				info.addStyleName("well");
				parentContents.add(info);

				// Add stats
				HTMLPanel stats = new HTMLPanel(result);
				stats.addStyleName("well");
				parentContents.add(stats);

				parentContents.add(contents);

				// Get friend's recent reviews
				if (FacebookTools.isLoggedIn()) {
					VendorFeedbackServiceAsync vendorFeedbackService = GWT.
							create(VendorFeedbackService.class);

					List<String> friendsIds =
							new ArrayList<String>(module.FACEBOOK_TOOLS.getUserFriends().keySet());
					vendorFeedbackService.getFriendsReviews(friendsIds, new AsyncCallback<List<VendorFeedback>>() {
						@Override
						public void onFailure(Throwable caught) {
							module.addMessage(true, "Could not load friends\' reviews! Reason: "
									+ caught.getMessage());
						}

						@Override
						public void onSuccess(List<VendorFeedback> result) {
							HTMLPanel reviews = new HTMLPanel("");
							reviews.addStyleName("well span6");

							if (result == null) {
								HTMLPanel response = new HTMLPanel("p", "No friends\' reviews could be found :(");
								response.addStyleName("lead pagination-centered");
								reviews.add(response);
							} else {
                                HTMLPanel response = new HTMLPanel("p", "Here are your friends\' reviews!");
                                response.addStyleName("lead pagination-centered");
                                reviews.add(response);

								Collections.sort(result, new FeedbackComparator());

								for (VendorFeedback f : result) {
									reviews.add(new ReviewWidget(module, f));
								}
							}
							contents.add(reviews);
						}
					});

					// Get friends' favourites
					FavouritesServiceAsync favouritesService = GWT.
							create(FavouritesService.class);

					favouritesService.getFriendsFavourites(friendsIds, new AsyncCallback<List<Favourite>>() {
						@Override
						public void onFailure(Throwable caught) {
							module.addMessage(true, "Could not load friends\' favourites! Reason: "
									+ caught.getMessage());
						}

						@Override
						public void onSuccess(List<Favourite> result) {
							HTMLPanel favs = new HTMLPanel("");
							favs.addStyleName("span6");

							if (result == null) {
								HTMLPanel response = new HTMLPanel("p", "No friends\' favourites could be found :(");
								response.addStyleName("lead pagination-centered");
								favs.addStyleName("well");
								favs.add(response);
							} else {
                                HTMLPanel response = new HTMLPanel("p", "Here are your friends\' favourites!");
                                response.addStyleName("lead pagination-centered well");
                                favs.add(response);

								SimpleTable table = new SimpleTable("table-hover table-bordered",
										"Key", "Name", "Friend");
								List<FavouriteButton> hearts = new LinkedList<FavouriteButton>();

								for (Favourite f : result) {
									String friendName = module.FACEBOOK_TOOLS.getUserFriends().get(f.getUserId());

									table.addRow(new PageClickHandler(module.VENDOR_PAGE, f.getVendorId()),
											f.getVendorId(), f.getVendorName(), friendName);

									FavouriteButton fb = new FavouriteButton(module, f.getVendorId(), f.getVendorName(),
											FacebookTools.getUserId());
									hearts.add(fb);
								}

								table.addWidgetColumn("", hearts);
								table.sortRows(0, false);

								favs.add(table);
							}

							contents.add(favs);
						}
					});


				}

				module.changeContent(parentContents);
			}
		});
	}

	class FeedbackComparator implements Comparator<VendorFeedback> {

		@Override
		public int compare(VendorFeedback f1, VendorFeedback f2) {
			return (int) (f2.getCreationTime() - f1.getCreationTime());
		}
	}
}
