package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/**
 * The contents of the Browse Vendors page
 */
public class BrowseVendorsContent implements Content {
    private static final String pageName = "Browse Vendors";
    private final BrowseVendorsServiceAsync browseVendorsService = GWT
            .create(BrowseVendorsService.class);

    @Override
    public String getPageName() {
        return pageName;
    }

    @Override
    public HTML getHTML(String input) {
        final HTML content = new HTML();

        browseVendorsService.browseVendorsServer(input,
            new AsyncCallback<String>() {
                public void onFailure(Throwable caught) {
                    content.setHTML("<p>Loading failed! Please try again.</p>");
                }

                public void onSuccess(String result) {
                    content.setHTML(result);
                }
            });

        return content;
    }
}
