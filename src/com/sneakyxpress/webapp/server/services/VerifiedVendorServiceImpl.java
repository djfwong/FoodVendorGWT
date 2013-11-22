package com.sneakyxpress.webapp.server.services;

import java.util.ArrayList;
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
        // Persist truck claim data
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT UNIQUE FROM "
                + VerifiedVendor.class.getName() + " WHERE vendorId == \""
                + v.getVendorId() + "\"");
        VerifiedVendor result = (VerifiedVendor) q.execute();

        if (result != null) {
            pm.deletePersistent(result);

            pm.makePersistent(v);

            pm.close();
            return true;
        } else {
            pm.makePersistent(v);

            pm.close();
            return false;
        }
	}

	@Override
	public boolean removeVerifiedVendor(String verifiedVendorId)
			throws IllegalArgumentException
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery("SELECT UNIQUE FROM "
					+ VerifiedVendor.class.getName() + " WHERE vendorId == \""
					+ verifiedVendorId + "\"");
			VerifiedVendor result = (VerifiedVendor) q.execute();

			if (result == null)
			{
				throw new IllegalArgumentException(
						"This user does not own this vendor");
			}

			pm.deletePersistent(result); // Remove the verified vendor

			pm.close();
			return true;
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "removeVerifiedVendor: " + e.getMessage());
			return false;
		}

	}

    @Override
    public VerifiedVendor getVerifiedVendor(String vendorId) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT UNIQUE FROM " + VerifiedVendor.class.getName()
                + " WHERE vendorId == \"" + vendorId + "\"");
        VerifiedVendor result = (VerifiedVendor) q.execute();

        return result;
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
		
		// to deal with RPC exception
		List<VerifiedVendor> vList = new ArrayList<VerifiedVendor>();
		for(VerifiedVendor v: result){
			vList.add(v);
		}

		pm.close();

		if (result.isEmpty())
		{
			return null;
		}
		else
		{
			return vList;
		}
	}
}
