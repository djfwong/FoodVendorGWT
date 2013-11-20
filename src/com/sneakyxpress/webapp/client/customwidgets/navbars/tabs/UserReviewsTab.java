package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.ReviewWidget;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.shared.User;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by michael on 11/14/2013.
 */
public class UserReviewsTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;
    private final User user;

    public UserReviewsTab(Sneaky_Xpress module, User user) {
        this.module = module;
        this.user = user;
    }

    @Override
    public String getTitle() {
        return "My Recent Reviews";
    }

    @Override
    public FlowPanel getContent() {
        final FlowPanel reviews = new FlowPanel();
        reviews.addStyleName("well");

        VendorFeedbackServiceAsync vendorFeedbackService = GWT
                .create(VendorFeedbackService.class);
        vendorFeedbackService.getUserReviews(user.getId(),
                new AsyncCallback<List<VendorFeedback>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, "Loading reviews failed! Reason: " +
                                caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<VendorFeedback> result) {
                        if (result == null) {
                            HTMLPanel response = new HTMLPanel("p", "No reviews could be found :(");
                            response.addStyleName("lead pagination-centered");
                            reviews.add(response);
                        } else {
                            HTMLPanel header = new HTMLPanel("p", "You have " + result.size() + " reviews!");
                            header.addStyleName("lead pagination-centered");
                            reviews.add(header);

                            Collections.sort(result, new FeedbackComparator());
                            for (VendorFeedback f : result) {
                                reviews.add(new ReviewWidget(module, f));
                            }
                        }
                    }
                });

        return reviews;
    }

    class FeedbackComparator implements Comparator<VendorFeedback> {

        @Override
        public int compare(VendorFeedback f1, VendorFeedback f2) {
            return (int) (f2.getCreationTime() - f1.getCreationTime());
        }
    }
}
