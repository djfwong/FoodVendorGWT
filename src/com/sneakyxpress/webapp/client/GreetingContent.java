package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

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
                    module.addMessage(GENERIC_ERROR_MESSAGE);
                }

                public void onSuccess(String result) {
                    module.changeContent(new HTML(result));
                }
            });
    }
}
