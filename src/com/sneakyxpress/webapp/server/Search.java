package com.sneakyxpress.webapp.server;

import com.sneakyxpress.webapp.shared.FoodVendor;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

public class Search {

	
	public void search(String keyword){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query q = pm.newQuery(FoodVendor.class);
		q.setFilter("name == input");
		q.declareParameters("String input");
		
		try {
			List<FoodVendor> results = (List<FoodVendor>) q.execute(keyword);
			if (!results.isEmpty()) {
				for (FoodVendor f: results) {
					// process result f
				}
			} else {
				// handle "no results" case
				}
			} finally {
				q.closeAll();
				}
		}
	
	
}
