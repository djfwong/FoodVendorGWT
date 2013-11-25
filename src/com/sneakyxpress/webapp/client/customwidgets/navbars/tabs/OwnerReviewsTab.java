package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
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
public class OwnerReviewsTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;
    private final User user;

    public OwnerReviewsTab(Sneaky_Xpress module, User user) {
        this.module = module;
        this.user = user;
    }

    @Override
    public String getTitle() {
        return "My Truck's Feedback";
    }

    @Override
    public FlowPanel getContent() {
        final FlowPanel feedback = new FlowPanel();
        
		HTMLPanel info = new HTMLPanel(
				"<p class=\"lead pagination-centered\">Business is booming today! Here's how all your trucks are doing!</p>");
		feedback .add(info);
        
        feedback.addStyleName("well");

        VendorFeedbackServiceAsync vendorFeedbackService = GWT.
                create(VendorFeedbackService.class);
        vendorFeedbackService.getVendorOwnerFeedback(user.getId(), new AsyncCallback<List<VendorFeedback>>() {
            @Override
            public void onFailure(Throwable caught) {
                module.addMessage(true, "Loading vendor feedback failed! Reason: " +
                        caught.getMessage());
            }

            @Override
            public void onSuccess(List<VendorFeedback> result) {
                if (result == null) {
                    HTMLPanel response = new HTMLPanel("p", "No feedback could be found :(");
                    response.addStyleName("lead pagination-centered");
                    feedback.add(response);
                } else {
                    double mean = 0.0;
                    for (VendorFeedback v : result) {
                        mean += v.getRating();
                    }
                    mean /= result.size();
                    String formatted = NumberFormat.getFormat("0.0").format(mean);

                    HTMLPanel header = new HTMLPanel("p", "You have " + result.size() + " reviews from users!<br>"
                            + "Your reviews average at " + formatted + " stars.");
                    header.addStyleName("lead pagination-centered");
                    feedback.add(header);

                    Collections.sort(result, new FeedbackComparator());
                    for (VendorFeedback f : result) {
                        feedback.add(new ReviewWidget(module, f));
                    }
                }
            }
        });

        return feedback;
    }

    class FeedbackComparator implements Comparator<VendorFeedback> {

        @Override
        public int compare(VendorFeedback f1, VendorFeedback f2) {
            return (int) (f1.getCreationTime() - f2.getCreationTime());
        }
    }
}
