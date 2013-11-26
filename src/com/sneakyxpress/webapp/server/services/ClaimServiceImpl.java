package com.sneakyxpress.webapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.TruckClaim;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClaimServiceImpl extends RemoteServiceServlet implements
        ClaimService {

    protected static final Logger logger = Logger.getLogger("");
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public List<TruckClaim> retrieveClaims() throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery();
        q.setClass(TruckClaim.class);

        @SuppressWarnings("unchecked")
        List<TruckClaim> results = (List<TruckClaim>) q.execute();

        pm.close();

        return new ArrayList<TruckClaim>(results);
    }

    @Override
    public boolean acceptClaim(long id) {
        try {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            TruckClaim tc = pm.getObjectById(TruckClaim.class, id);

            Query q = pm.newQuery();
            q.setClass(TruckClaim.class);
            List<TruckClaim> results = (List<TruckClaim>) q.execute();
            results = new ArrayList<TruckClaim>(results);

            boolean isTaken = false;
            for (TruckClaim c : results) {
                if (c.getTruckId().equals(tc.getTruckId()) && !c.getId().equals(id) && c.isAccepted()) {
                    isTaken = true;
                    break;
                }
            }

            if (isTaken) {
                tc.setViewed(true);
                logger.log(Level.WARNING, "Truck has been claimed, can't accept claim");
                pm.close();
                return false;
            }

            tc.setAccepted(true);
            tc.setViewed(true);

            pm.close();
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "acceptClaim " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rejectClaim(Long claimId) {
        try {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            TruckClaim tc = pm.getObjectById(TruckClaim.class, claimId);
            tc.setAccepted(false);
            tc.setViewed(true);

            try {
                // Rejecting a previously accepted claim
                removeFromVerifiedVendors(pm, tc.getFacebookId(),
                        tc.getTruckId());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "rejectClaim: " + e.getMessage());
                return false;
            }

            pm.close();
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "rejectClaim: " + e.getMessage());
            return false;
        }
    }

    public boolean removeFromVerifiedVendors(PersistenceManager pm,
                                             String userId, String truckId) {
        try {
            List<VerifiedVendor> v = searchVerifiedVendors(pm, userId, truckId);

            // Should only be 1 entry for each combination of user and truck id
            if (v.size() > 0) {
                pm.deletePersistent(v.get(0));
                return true;
            }

            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE,
                    "removeFromVerifiedVendors: " + e.getMessage());
            return false;
        }
    }

    public List<VerifiedVendor> searchVerifiedVendors(PersistenceManager pm,
                                                      String userId, String truckId) {
        try {
            // Search for a particular verified ID with certain user id and
            // truck id
            Query q = pm.newQuery(VerifiedVendor.class);
            q.setFilter("userId == :fbid && vendorId == :truckid");

            Map<String, String> paramValues = new HashMap<String, String>();
            paramValues.put("fbid", userId);
            paramValues.put("truckid", truckId);

            @SuppressWarnings("unchecked")
            List<VerifiedVendor> v = (List<VerifiedVendor>) q
                    .executeWithMap(paramValues);

            return v;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
}