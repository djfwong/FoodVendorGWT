package com.sneakyxpress.webapp.client.pages.greeting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.customwidgets.ReviewWidget;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
                    HTMLPanel contents = new HTMLPanel("");

                    // Add stats
                    HTMLPanel stats = new HTMLPanel(result);
                    stats.addStyleName("well");
                    contents.add(stats);

                    // Get friend's recent activity
                    if (FacebookTools.isLoggedIn()) {
                        VendorFeedbackServiceAsync vendorFeedbackService = GWT.
                                create(VendorFeedbackService.class);

                        List<String> friendsIds =
                                new ArrayList<String>(module.FACEBOOK_TOOLS.getUserFriends().values());
                        vendorFeedbackService.getFriendsReviews(friendsIds, new AsyncCallback<List<VendorFeedback>>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                module.addMessage(true, "Could not load friends\' reviews! Reason: "
                                        + caught.getMessage());
                            }

                            @Override
                            public void onSuccess(List<VendorFeedback> result) {
                                if (result != null) {
                                    HTMLPanel reviews = new HTMLPanel("");
                                    reviews.addStyleName("well");

                                    Collections.sort(result, new FeedbackComparator());

                                    for (VendorFeedback f : result) {
                                        reviews.add(new ReviewWidget(module, f));
                                    }
                                }
                            }
                        });
                    }
                    module.changeContent(contents);
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
