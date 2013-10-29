package com.sneakyxpress.webapp.client.search;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.FoodVendorDisplayTable;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.shared.FoodVendor;

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
        searchService.searchServer(input, new AsyncCallback<List<FoodVendor>>() {
                public void onFailure(Throwable caught) {
                    module.addMessage(GENERIC_ERROR_MESSAGE);
                }

                public void onSuccess(List<FoodVendor> search_results) {
                	HTMLPanel content = new HTMLPanel("");
                	
                	if (!search_results.isEmpty()){
                		Widget table = new FoodVendorDisplayTable(search_results, module.getVendorPage()).getWidget();
                		content.add(table);
                	} else {
                		Widget no_matches = new Label("Sorry, no matches were found :(");
                		content.add(no_matches);
                	}
                	
                    module.changeContent(content);
                }
            });
    }
    
    
}
