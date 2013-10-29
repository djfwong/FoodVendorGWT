package com.sneakyxpress.webapp.client.viewvendor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("viewVendor")
public interface ViewVendorService extends RemoteService {
    FoodVendor viewVendorServer(String id) throws IllegalArgumentException;
}
