package com.sneakyxpress.webapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.TruckClaim;

public class ClaimServiceImpl extends RemoteServiceServlet implements
ClaimService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<TruckClaim> retrieveClaims() throws IllegalArgumentException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery();
		q.setClass(TruckClaim.class);

		@SuppressWarnings("unchecked")
		List<TruckClaim> results = (List<TruckClaim>) q.execute();

		pm.close();

		return new ArrayList<TruckClaim>(results);
	}

	@Override
	public boolean acceptClaim(String fbId, String truckId)
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();

			long id = searchForClaim(fbId, truckId);
			TruckClaim tc = (TruckClaim) pm.getObjectById(id);
			tc.setAccepted(true);

			//mark as claim viewed as well
			setViewed(pm, id);

			pm.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}


	@Override
	public boolean rejectClaim(String fbId, String truckId)
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();

			long id = searchForClaim(fbId, truckId);
			TruckClaim tc = (TruckClaim) pm.getObjectById(id);
			tc.setAccepted(false);

			//mark as claim viewed as well
			setViewed(pm, id);

			pm.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * 
	 * @param fbId - userID
	 * @param truckId - truckID
	 * @return the PK auto-generated id associated with entry for editing an object in other method calls
	 */
	public long searchForClaim(String fbId, String truckId)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(TruckClaim.class,
				"facebookId == fbId && truckId == truckId");
		q.declareParameters("String lastNameParam");
		q.declareParameters("String truckId");

		TruckClaim results = (TruckClaim) q.execute(fbId, truckId);

		return results.getId();

	}

	public void setViewed(PersistenceManager pm, long id){

		TruckClaim tc = (TruckClaim) pm.getObjectById(id);
		tc.setViewed(true);

	}
}