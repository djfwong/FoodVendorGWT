package com.sneakyxpress.webapp.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.facebook.PersistUserService;
import com.sneakyxpress.webapp.shared.User;

/**
 * Updates the Food Vendor data from DataVancouver
 */
public class PersistUserServiceImpl extends RemoteServiceServlet implements PersistUserService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("");


	@Override
	public String persistNewUserToDatastore(User user)
			throws IllegalArgumentException {
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			// Add non-duplicate new user to app engine datastore
			Query q = pm.newQuery(User.class, "id == idParam");
			q.declareParameters("String idParam");
			
			// Search by id 
			List<User> results = (List<User>) q.execute(user.getId());

			// Add to datastore
			if (results.isEmpty()) {
				pm.makePersistentAll(user);
	
				// Clean-up
				pm.close();
			}
			
			else {
				logger.log(Level.INFO, "User email already in datastore");
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}
}
