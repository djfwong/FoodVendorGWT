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
    	String queryString1 = String.format("name.startsWith('%s')", input);
		Query q1 = pm.newQuery(FoodVendor.class, queryString1);
		
		try {
			List<FoodVendor> results = (List<FoodVendor>) q1.execute();
			if (!results.isEmpty()) {
				for (FoodVendor f: results) {
					// process result f
					search_results.add(f);
				}
			} else {
			}
		} finally {
				q1.closeAll();
				}
		
		String queryString2 = String.format("location.startsWith('%s')", input);
		Query q2 = pm.newQuery(FoodVendor.class, queryString2);
		
		try {
			List<FoodVendor> results = (List<FoodVendor>) q2.execute();
			if (!results.isEmpty()) {
				for (FoodVendor f: results) {
					// process result f
					if (!containedInResults(search_results, f)){
					search_results.add(f);
					}
				}
			} else {
			}
		} finally {
				q2.closeAll();
				}
		
		String queryString3 = String.format("description.startsWith('%s')", input);
		Query q3 = pm.newQuery(FoodVendor.class, queryString3);
		
		try {
			List<FoodVendor> results = (List<FoodVendor>) q3.execute();
			if (!results.isEmpty()) {
				for (FoodVendor f: results) {
					// process result f
					if (!containedInResults(search_results, f)){
					search_results.add(f);
					}
				}
			} else {
			}
		} finally {
				q3.closeAll();
				}

		return search_results;
    	
        //return "<h1>Search results (stub) for input: " + input + "</h1>";
    }
    
    private boolean containedInResults(List<FoodVendor> search_results, FoodVendor f){
    	for (FoodVendor fv : search_results){
    		if (f.getVendorId() == fv.getVendorId()){
    			return true;
    		}
    	} return false;
    }
}
