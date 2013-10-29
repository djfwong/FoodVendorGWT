package com.sneakyxpress.webapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The async counterpart of ViewVendorService
 */
public interface ViewVendorServiceAsync {
    void viewVendorServer(String id, AsyncCallback<FoodVendor> callback)
            throws IllegalArgumentException;
}
