package com.sneakyxpress.webapp.server.pages;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.sneakyxpress.webapp.server.PMF;
import org.apache.commons.lang3.StringUtils;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.pages.search.SearchService;
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
    		if (StringUtils.containsIgnoreCase(f.getName(), input)){
    			search_results.add(f);
    		} else if (StringUtils.containsIgnoreCase(f.getDescription(), input)){
    			search_results.add(f);
    		} else if (StringUtils.containsIgnoreCase(f.getLocation(), input)){
    			search_results.add(f);
    		}
    	}
    	
		return search_results;
    	
    }
    
}
