package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Get the content of our search results
 */
public class SearchContent implements Content {
    private static final String pageName = "Search Results";
    private static final String pageStub = "search";

    private final SearchServiceAsync searchService = GWT.create(SearchService.class);

    @Override
    public String getPageName() {
        return pageName;
    }

    @Override
    public String getPageStub() {
        return pageStub;
    }

    @Override
    public Widget getContent(String input) {
        final HTML content = new HTML();

        searchService.searchServer(input,
                new AsyncCallback<String>() {
                    public void onFailure(Throwable caught) {
                        content.setHTML(GENERIC_ERROR_MESSAGE);
                    }

                    public void onSuccess(String result) {
                        content.setHTML(result);
                    }
                });

        return content;
    }
}
