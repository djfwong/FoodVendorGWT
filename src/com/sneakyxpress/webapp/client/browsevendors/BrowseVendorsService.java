package com.sneakyxpress.webapp.client.browsevendors;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.FoodVendor;

import java.util.List;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("browseVendors")
public interface BrowseVendorsService extends RemoteService {
    List<FoodVendor> browseVendorsServer(String name) throws IllegalArgumentException;
}
