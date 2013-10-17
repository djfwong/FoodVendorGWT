package com.sneakyxpress.webapp.server;

import com.sneakyxpress.webapp.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
        return "<h1>This is (a stub for) the homepage!!!<h1>";
    }
}
