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
    public List<VendorFeedback> getVendorFeedback(String userId)
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
                    + " WHERE vendorId == \"" + v.getVendorId() + "\"");
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
}
