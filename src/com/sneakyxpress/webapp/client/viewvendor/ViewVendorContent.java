package com.sneakyxpress.webapp.client.viewvendor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
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
                        HTMLPanel content = new HTMLPanel(""); // The base panel to hold all content

                        // Contains the basic information of the Food Vendor
                        HTMLPanel info = new HTMLPanel("");
                        info.addStyleName("row-fluid");

                        // The basic text information
                        HTMLPanel textInfo = new HTMLPanel("");
                        textInfo.addStyleName("span6");

                        String name = vendor.getName();
                        if (name.isEmpty()) {
                            name = "<em class=\"muted\">No Name</em>";
                        }
                        textInfo.add(new HTML("<h2>" + name + "</h2><hr style=\"padding-bottom: 10px\">"));

                        textInfo.add(getInfoWidget("Description", vendor.getDescription()));
                        textInfo.add(getInfoWidget("Location", vendor.getLocation()));

                        // A simple map
                        HTMLPanel mapInfo = new HTMLPanel("");
                        mapInfo.addStyleName("span6");
                        mapInfo.add(new HTML("<p class=\"muted\">Stub for the vendor map!</p>"));

                        // Group all the basic information together
                        info.add(textInfo);
                        info.add(mapInfo);

                        // TODO: Reviews will go here

                        // Group all of our content together & change the content of the page
                        content.add(info);
                        module.changeContent(content);
                    }
                });
    }

    private Widget getInfoWidget(String title, String info) {
        if (info.isEmpty()) {
            info = "<em class=\"muted\">Information currently not available.</em>";
        }

        return new HTML("<p><strong>" + title + "</strong><br>" + info + "</p>");
    }
}
