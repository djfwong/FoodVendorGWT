package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.shared.FoodVendor;

import java.util.List;

/**
 * Created by michael on 11/19/2013.
 */
public class RecentVendorsTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;

    public RecentVendorsTab(Sneaky_Xpress module) {
        this.module = module;
    }

    @Override
    public String getTitle() {
        return "Recently Viewed";
    }

    @Override
    public FlowPanel getContent() {
        FlowPanel recent = new FlowPanel();
        recent.addStyleName("well");

        List<FoodVendor> recentlyViewed = module.FACEBOOK_TOOLS.getRecentlyViewed();

        if (recentlyViewed.isEmpty()) {
            HTMLPanel response = new HTMLPanel("p", "No recently viewed vendors could be found :(");
            response.addStyleName("lead pagination-centered");
            recent.add(response);
        } else {
            for (FoodVendor v : recentlyViewed) {
                recent.add(getVendorWidget(v));
            }
        }

        return recent;
    }

    private HTMLPanel getVendorWidget(FoodVendor v) {
        HTMLPanel vendor = new HTMLPanel("p", v.getName() + " ");
        vendor.addStyleName("lead");

        Anchor vendorLink = new Anchor("View");
        vendorLink.addClickHandler(new PageClickHandler(module.VENDOR_PAGE, v.getVendorId()));
        vendorLink.addStyleName("pull-right btn btn-info");

        vendor.add(vendorLink);

        return vendor;
    }
}
