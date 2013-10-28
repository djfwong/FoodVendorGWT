package com.sneakyxpress.webapp.server;

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

    public String searchServer(String input) throws IllegalArgumentException {
    	
    	String search_results = "<h1>Search results for " + input + ":</h1>";
    	PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(FoodVendor.class);
		q.setFilter("name == input");
		q.declareParameters("String input");
		
		try {
			List<FoodVendor> results = (List<FoodVendor>) q.execute(input);
			if (!results.isEmpty()) {
				for (FoodVendor f: results) {
					// process result f
					search_results = search_results.concat("<br>" + f.getName());
				}
			} else {
				// handle "no results" case
				search_results = "<h1>Sorry, no food vendors match your search. Maybe you spelled it wrong.</h1>";
				}
			} finally {
				q.closeAll();
				}
		return search_results;
    	
        //return "<h1>Search results (stub) for input: " + input + "</h1>";
    }
}
