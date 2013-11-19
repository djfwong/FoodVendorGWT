package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
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
        final FlowPanel statistics = new FlowPanel();



        return statistics;
    }
}
