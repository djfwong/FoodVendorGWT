package com.sneakyxpress.webapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("BrowseVendors")
public interface BrowseVendorsService extends RemoteService {
    String browseVendorsServer(String name) throws IllegalArgumentException;
}
