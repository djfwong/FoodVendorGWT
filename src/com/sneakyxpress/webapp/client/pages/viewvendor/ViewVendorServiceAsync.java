package com.sneakyxpress.webapp.client.pages.viewvendor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The async counterpart of ViewVendorService
 */
public interface ViewVendorServiceAsync {
    void viewVendorServer(String id, AsyncCallback<FoodVendor> callback)
            throws IllegalArgumentException;
}
