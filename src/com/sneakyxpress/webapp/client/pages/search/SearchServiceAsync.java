package com.sneakyxpress.webapp.client.pages.search;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The async counterpart of <code>SearchService</code>.
 */
public interface SearchServiceAsync {
    void searchServer(String input, AsyncCallback<List<FoodVendor>> callback)
            throws IllegalArgumentException;
}
