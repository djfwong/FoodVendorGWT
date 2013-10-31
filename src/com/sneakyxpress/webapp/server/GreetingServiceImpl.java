package com.sneakyxpress.webapp.server;

import com.sneakyxpress.webapp.client.greeting.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.shared.FoodVendor;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery();
        q.setClass(FoodVendor.class);

        List<FoodVendor> results = (List<FoodVendor>) q.execute();

		return "<p class=\"lead\">Vancouver Food Vendor Reviews currently has information on "
                + String.valueOf(results.size()) + " food vendors!";
    }
}
