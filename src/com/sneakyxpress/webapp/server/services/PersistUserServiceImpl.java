package com.sneakyxpress.webapp.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.User;
import com.sneakyxpress.webapp.shared.VendorFeedback;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

/**
 * Updates the Food Vendor data from DataVancouver
 */
public class PersistUserServiceImpl extends RemoteServiceServlet implements
		PersistUserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger logger = Logger.getLogger("");

	@Override
	public boolean persistNewUserToDatastore(User user)
			throws IllegalArgumentException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery("SELECT UNIQUE FROM " + User.class.getName()
				+ " WHERE id == \"" + user.getId() + "\"");
		User result = (User) q.execute();

		boolean existingMember = result != null;
		if (existingMember)
		{
			// Update the existing user's information
			result.setName(user.getName());
			result.setEmail(user.getEmail());
		}
		else
		{
			// Create the new user
			pm.makePersistent(user);
		}

		pm.close();
		return existingMember;
	}

	/**
	 * 
	 * @param fbId
	 * @param type
	 *            1-Admin, 2-Owner, 3-User, 4-Admin&Owner as defined in User
	 *            class
	 * @return
	 */
	public boolean changeUserStatus(String fbId, int type)
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			User user = pm.getObjectById(User.class, fbId);
			user.setType(type);
			pm.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public List<User> getAllUsers() throws IllegalArgumentException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery();
		q.setClass(User.class);

		@SuppressWarnings("unchecked")
		List<User> results = (List<User>) q.execute();

		pm.close();

		return new ArrayList<User>(results);
	}

	@Override
	public boolean removeUser(String userId)
	{
        boolean foundUser = true;

        // Delete the user
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q1 = pm.newQuery("SELECT UNIQUE FROM " + User.class.getName()
                + " WHERE id == \"" + userId + "\"");
        User r1 = (User) q1.execute();

        if(r1 == null) {
            foundUser = false;
        } else {
            pm.deletePersistent(r1);
        }

        // Delete their favourites
        Query q2 = pm.newQuery("SELECT FROM " + Favourite.class.getName()
                + " WHERE userId == \"" + userId + "\"");
        List<Favourite> r2 = (List<Favourite>) q2.execute();
        r2 = new ArrayList<Favourite>(r2);

        if (!r2.isEmpty()) {
            pm.deletePersistentAll(r2);
        }

        // Delete their reviews
        Query q3 = pm.newQuery("SELECT FROM " + VendorFeedback.class.getName()
                + " WHERE authorId == \"" + userId + "\"");
        List<VendorFeedback> r3 = (List<VendorFeedback>) q3.execute();
        r3 = new ArrayList<VendorFeedback>(r3);

        if (!r3.isEmpty()) {
            pm.deletePersistentAll(r3);
        }

        // Delete their verified vendors
        Query q4 = pm.newQuery("SELECT FROM " + VerifiedVendor.class.getName()
                + " WHERE userId == \"" + userId + "\"");
        List<VerifiedVendor> r4 = (List<VerifiedVendor>) q4.execute();
        r4 = new ArrayList<VerifiedVendor>(r4);

        if (!r4.isEmpty()) {
            pm.deletePersistentAll(r4);
        }

        // Clean up
        pm.close();

		return foundUser;

	}

}
