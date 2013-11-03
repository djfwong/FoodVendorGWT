package com.sneakyxpress.webapp.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.search.SearchService;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The server side implementation of the RPC service.
 */
public class SearchServiceImpl extends RemoteServiceServlet implements
        SearchService {

    public List<FoodVendor> searchServer(String input) throws IllegalArgumentException {
    	
    	List<FoodVendor> search_results = new ArrayList<FoodVendor>();
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	Query q1 = pm.newQuery(FoodVendor.class);
    	List<FoodVendor> all_vendors = (List<FoodVendor>) q1.execute();
    	for (FoodVendor f : all_vendors){
    		if (f.getName().contains(input)){
    			search_results.add(f);
    		} else if (f.getDescription().contains(input)){
    			search_results.add(f);
    		} else if (f.getLocation().contains(input)){
    			search_results.add(f);
    		}
    	}
    	
		return search_results;
    	
    }
    
}
