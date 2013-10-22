package com.sneakyxpress.webapp.server;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

public class Search {

	
	public void search(String keyword){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query q = pm.newQuery(FoodVendor.class);
		q.setFilter("Food Vendor ==" + keyword);
		
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
