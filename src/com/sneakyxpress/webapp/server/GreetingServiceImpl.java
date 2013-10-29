package com.sneakyxpress.webapp.server;

import com.sneakyxpress.webapp.client.greeting.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		return "<h1>This is (a stub for) the homepage!!!</h1>"
        		+ "<br />"
        		+ "<input type=\"button\" value=\"Clear POIs\" onclick=\"clearOverlays();\">"
        		+ "<input type=\"button\" value=\"Show POIs\" onclick=\"showOverlays();\">"
        		+ "<br />"
        		+ "<div id=\"map_canvas\" style=\"width: 100%; height: 500px;\"></div>";
    }
}
