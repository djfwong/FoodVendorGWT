package com.sneakyxpress.webapp.client.pages.search;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.*;
import com.sneakyxpress.webapp.client.customwidgets.FavouriteButton;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
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
                    module.addMessage(true, GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
                }

                public void onSuccess(List<FoodVendor> search_results) {
                	HTMLPanel content = new HTMLPanel("");
                	
                	if (!search_results.isEmpty()){
                        SimpleTable table = new SimpleTable("table-hover table-bordered table-striped",
                                "Key", "Name", "Description", "Location");
                        for (FoodVendor v : search_results) {
                            table.addRow(new PageClickHandler(module.VENDOR_PAGE, v.getVendorId()),
                                    v.getVendorId(), v.getName(), v.getDescription(), v.getLocation());
                        }

                        List<FavouriteButton> hearts = new LinkedList<FavouriteButton>();
                        for (FoodVendor v : search_results) {
                            FavouriteButton fb = new FavouriteButton(module, v, FacebookTools.getUserId(), v.getDescription());
                            hearts.add(fb);
                        }
                        table.addWidgetColumn("", hearts);

                        table.sortRows(0, false);

                		content.add(table);
                        module.changeContent(content);
                	} else {
                		content.add(new HTML("<p class=\"lead\" style=\"text-align: center;\">" +
                                "Sorry, no matches were found :(</p></div>"));
                	}

                    module.changeContent(content);
                }
            });
    }
    
    
}
