package com.sneakyxpress.webapp.server.pages;

import com.sneakyxpress.webapp.client.pages.greeting.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.FoodVendor;
import com.sneakyxpress.webapp.shared.User;

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

        List<FoodVendor> vendors = (List<FoodVendor>) q.execute();

        q = pm.newQuery();
        q.setClass(User.class);

        List<User> users = (List<User>) q.execute();

        pm.close();

		return "<p class=\"lead pagination-centered\">Vancouver Food Vendor Reviews currently has information on "
                + String.valueOf(vendors.size()) + " food vendors!</p>"
                + "<p class=\"lead pagination-centered\">We thank our " + users.size() + " users for their support!</p>";
    }
}
