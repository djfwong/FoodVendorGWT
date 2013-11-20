package com.sneakyxpress.webapp.server.services;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.TruckClaim;
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

	@Override
	public boolean addVerifiedVendor(VerifiedVendor v)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try
		{
			// Check to make sure truckId is not already there
			Query q = pm.newQuery(TruckClaim.class,
					"facebookId == fbId && truckId == truckId");
			q.declareParameters("String lastNameParam");
			q.declareParameters("String truckId");

			TruckClaim result = (TruckClaim) q.execute(v.getId(),
					v.getVendorId());

			if (result != null)
			{
				return false;
			}

			else
			{
				pm.makePersistent(v);
				return true;
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
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
