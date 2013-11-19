package com.sneakyxpress.webapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.VendorFeedback;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/14/2013.
 */
public class VendorFeedbackServiceImpl extends RemoteServiceServlet
        implements VendorFeedbackService {


    @Override
    public List<VendorFeedback> getVendorReviews(String vendorId) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT FROM " + VendorFeedback.class.getName()
                + " WHERE ownerVendorId == \"" + vendorId + "\"");
        List<VendorFeedback> result = (List<VendorFeedback>) q.execute();

        pm.close();

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    /**
     * Gets the reviews written about a particular vendor owner's trucks
     *
     * @param userId        The vendor owner
     * @return              All the reviews about the vendor owner's trucks
     * @throws IllegalArgumentException
     */
    @Override
    public List<VendorFeedback> getVendorOwnerFeedback(String userId)
            throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q1 = pm.newQuery("SELECT FROM " + VerifiedVendor.class.getName()
                + " WHERE userId == \"" + userId + "\"");
        List<VerifiedVendor> verifiedVendors = (List<VerifiedVendor>) q1.execute();

        if (verifiedVendors.isEmpty()) {
            return null;
        }

        List<VendorFeedback> feedback = new ArrayList<VendorFeedback>();
        for (VerifiedVendor v : verifiedVendors) {
            Query q2 = pm.newQuery("SELECT FROM " + VendorFeedback.class.getName()
                    + " WHERE ownerVendorId == \"" + v.getVendorId() + "\"");
            List<VendorFeedback> result = (List<VendorFeedback>) q2.execute();
            feedback.addAll(result);
        }

        pm.close();

        if (feedback.isEmpty()) {
            return null;
        } else {
            return feedback;
        }
    }

    /**
     * Get the reviews a particular user wrote
     *
     * @param userId        The user who wrote the review
     * @return              All reviews the user wrote
     * @throws IllegalArgumentException
     */
    @Override
    public List<VendorFeedback> getUserReviews(String userId)
            throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT FROM " + VendorFeedback.class.getName()
                + " WHERE authorUserId == \"" + userId + "\"");
        List<VendorFeedback> result = (List<VendorFeedback>) q.execute();

        pm.close();

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }
}
