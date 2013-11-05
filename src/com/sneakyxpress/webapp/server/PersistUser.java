package com.sneakyxpress.webapp.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.shared.User;

/**
 * Updates the Food Vendor data from DataVancouver
 */
public class PersistUser extends RemoteServiceServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("");

	public boolean persistNewUserToDatastore(User user) {
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			// Add non-duplicate new user to app engine datastore
			Query q = pm.newQuery(User.class, "email == emailParam");
			q.declareParameters("String emailParam");
			List<User> results = (List<User>) q.execute(user.getEmail());

			if (results.isEmpty()) {
				pm.makePersistentAll(user);

				// Clean-up
				pm.close();
				return true;
			}

			else {
				logger.log(Level.INFO, "User email already in datastore");
				return false;
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return false;
		}

	}
}
