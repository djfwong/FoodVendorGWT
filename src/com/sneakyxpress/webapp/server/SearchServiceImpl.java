package com.sneakyxpress.webapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.SearchService;

/**
 * The server side implementation of the RPC service.
 */
public class SearchServiceImpl extends RemoteServiceServlet implements
        SearchService {

    public String searchServer(String input) throws IllegalArgumentException {
        return "<h1>Search results (stub) for input: " + input + "</h1>";
    }
}
