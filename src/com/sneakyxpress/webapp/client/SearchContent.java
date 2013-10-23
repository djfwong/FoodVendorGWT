package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/**
 * Get the content of our search results
 */
public class SearchContent extends Content {
    private static final String pageName = "Search Results";
    private static final String pageStub = "search";

    private final SearchServiceAsync searchService = GWT.create(SearchService.class);

    public SearchContent(Sneaky_Xpress module) {
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
        searchService.searchServer(input, new AsyncCallback<String>() {
                public void onFailure(Throwable caught) {
                    module.addMessage(GENERIC_ERROR_MESSAGE);
                }

                public void onSuccess(String result) {
                    module.changeContent(new HTML(result));
                }
            });
    }
}
