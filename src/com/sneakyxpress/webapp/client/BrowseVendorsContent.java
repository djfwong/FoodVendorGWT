package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.shared.FoodVendor;

import java.util.List;

/**
 * The contents of the Browse Vendors page
 */
public class BrowseVendorsContent extends Content {
    private static final String pageName = "Browse Vendors";
    private static final String pageStub = "vendors";

    private final BrowseVendorsServiceAsync browseVendorsService = GWT.create(BrowseVendorsService.class);

    public BrowseVendorsContent(Sneaky_Xpress module) {
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
        browseVendorsService.browseVendorsServer(input, new AsyncCallback<List<FoodVendor>>() {
            public void onFailure(Throwable caught) {
                module.addMessage(GENERIC_ERROR_MESSAGE);
            }

            public void onSuccess(List<FoodVendor> result) {
                HTMLPanel panel = new HTMLPanel("");
                for (FoodVendor v : result) {
                    HTML a = new HTML();
                    a.setHTML("<p>" + v.getName() + "</p>");
                    panel.add(a);
                }
                module.changeContent(panel);
            }
        });
    }
}
