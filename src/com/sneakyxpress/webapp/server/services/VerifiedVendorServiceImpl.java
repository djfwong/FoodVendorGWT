package com.sneakyxpress.webapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Updates the Food Vendor data from DataVancouver
 */
public class VerifiedVendorServiceImpl extends RemoteServiceServlet
        implements VerifiedVendorService {

    @Override
    public void removeVerifiedVendor(Long verifiedVendorId) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT UNIQUE FROM " + VerifiedVendor.class.getName()
                + " WHERE id == \"" + verifiedVendorId + "\"");
        VerifiedVendor result = (VerifiedVendor) q.execute();

        if (result == null) {
            throw new IllegalArgumentException("This user does not own this vendor");
        }

        pm.deletePersistent(result); // Remove the verified vendor

        pm.close();
    }

    @Override
    public List<VerifiedVendor> getVerifiedVendors(String userId) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT FROM " + VerifiedVendor.class.getName()
                + " WHERE userId == \"" + userId + "\"");
        List<VerifiedVendor> result = (List<VerifiedVendor>) q.execute();

        pm.close();

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }
}
