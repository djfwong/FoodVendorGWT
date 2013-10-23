package com.sneakyxpress.webapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.FoodVendor;

import java.util.List;

/**
 * The async counterpart of <code>BrowseVendorsService</code>.
 */
public interface BrowseVendorsServiceAsync {
    void browseVendorsServer(String input, AsyncCallback<List<FoodVendor>> callback)
            throws IllegalArgumentException;
}
