package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/**
 * Get the content for our home page.
 */
public class GreetingContent implements Content {
    private static final String pageName = "Vancouver Food Vendor Reviews";
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);

    @Override
    public String getPageName() {
        return pageName;
    }

    @Override
    public HTML getHTML(String input) {
        final HTML content = new HTML();

        greetingService.greetServer(input,
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
