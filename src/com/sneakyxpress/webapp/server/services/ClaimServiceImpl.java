package com.sneakyxpress.webapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.TruckClaim;

public class ClaimServiceImpl extends RemoteServiceServlet implements ClaimService{

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
	
}