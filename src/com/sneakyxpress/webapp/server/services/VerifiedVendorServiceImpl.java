package com.sneakyxpress.webapp.server.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

/**
 * Updates the Food Vendor data from DataVancouver
 */
public class VerifiedVendorServiceImpl extends RemoteServiceServlet implements
		VerifiedVendorService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Logger logger = Logger.getLogger("");

	@Override
	public boolean addVerifiedVendor(VerifiedVendor v)
	{
		try
		{
			// Persist truck claim data
			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery(VerifiedVendor.class);
			q.setFilter("userId == :fbid && vendorId == :truckid");

			Map<String, String> paramValues = new HashMap<String, String>();
			paramValues.put("fbid", v.getUserId());
			paramValues.put("truckid", v.getVendorId());

			@SuppressWarnings("unchecked")
			List<VerifiedVendor> vList = (List<VerifiedVendor>) q
					.executeWithMap(paramValues);

			if (vList.size() == 0)
			{
				// Check Same FB Id and Truck Id is not already in datastore
				pm.makePersistent(v);

				pm.close();
				return true;
			}
			else
			{
				logger.log(Level.INFO, "addVerifiedVendor: Possible Duplicate");
				return false;
			}
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "addVerifiedVendor: " + e.getMessage());
			return false;
		}
	}

	@Override
	public void removeVerifiedVendor(String verifiedVendorId)
			throws IllegalArgumentException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery("SELECT UNIQUE FROM "
				+ VerifiedVendor.class.getName() + " WHERE id == \""
				+ verifiedVendorId + "\"");
		VerifiedVendor result = (VerifiedVendor) q.execute();

		if (result == null)
		{
			throw new IllegalArgumentException(
					"This user does not own this vendor");
		}

		pm.deletePersistent(result); // Remove the verified vendor

		pm.close();
	}

	@Override
	public List<VerifiedVendor> getVerifiedVendors(String userId)
			throws IllegalArgumentException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery("SELECT FROM " + VerifiedVendor.class.getName()
				+ " WHERE userId == \"" + userId + "\"");

		@SuppressWarnings("unchecked")
		List<VerifiedVendor> result = (List<VerifiedVendor>) q.execute();

		pm.close();

		if (result.isEmpty())
		{
			return null;
		}
		else
		{
			return result;
		}
	}
}
