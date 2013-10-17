package com.sneakyxpress.webapp.server;

import com.sneakyxpress.webapp.client.BrowseVendorsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
public class BrowseVendorsServiceImpl extends RemoteServiceServlet implements
        BrowseVendorsService {

    public String browseVendorsServer(String input) throws IllegalArgumentException {
        return "<h1>Browse Vendors Page!!!</h1>";
    }
}
