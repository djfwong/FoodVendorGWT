package com.sneakyxpress.webapp.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.TruckClaim;
import com.sneakyxpress.webapp.shared.User;

public class ClaimServiceImpl extends RemoteServiceServlet implements
		ClaimService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Logger logger = Logger.getLogger("");

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
	public boolean acceptClaim(long id)
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			TruckClaim tc = pm.getObjectById(TruckClaim.class, id);
			tc.setAccepted(true);
			tc.setViewed(true);

			pm.close();
			return true;
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "acceptClaim " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean rejectClaim(Long claimId)
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			TruckClaim tc = pm.getObjectById(TruckClaim.class, claimId);
			tc.setAccepted(false);
			tc.setViewed(true);

			pm.close();
			return true;
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "rejectClaim: " + e.getMessage());
			return false;
		}
	}
}