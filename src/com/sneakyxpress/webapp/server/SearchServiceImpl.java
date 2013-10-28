package com.sneakyxpress.webapp.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.SearchService;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The server side implementation of the RPC service.
 */
public class SearchServiceImpl extends RemoteServiceServlet implements
        SearchService {

    public List<FoodVendor> searchServer(String input) throws IllegalArgumentException {
    	
    	List<FoodVendor> search_results = new ArrayList<FoodVendor>();
    	PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(FoodVendor.class);
		q.setFilter("name == input");
		q.declareParameters("String input");
		
		try {
			List<FoodVendor> results = (List<FoodVendor>) q.execute(input);
			if (!results.isEmpty()) {
				for (FoodVendor f: results) {
					// process result f
					search_results.add(f);
				}
			} else {
			}
		} finally {
				q.closeAll();
				}
		return search_results;
    	
        //return "<h1>Search results (stub) for input: " + input + "</h1>";
    }
}
