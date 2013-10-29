package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The UI of the Food Vendor pages.
 */
public class ViewVendorContent extends Content {
    private static final String pageName = "View Vendor";
    private static final String pageStub = "view";

    private final ViewVendorServiceAsync viewVendorService = GWT.create(ViewVendorService.class);

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
                        module.addMessage(GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(FoodVendor vendor) {
                        // TODO: Implement UI! The vendor here is the vendor we want to show.
                        HTML stub = new HTML();
                        stub.setHTML("<p>Success!!!</p>");
                        module.changeContent(stub);
                    }
                });
    }
}
