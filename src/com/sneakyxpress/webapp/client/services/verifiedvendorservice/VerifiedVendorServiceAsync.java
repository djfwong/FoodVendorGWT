package com.sneakyxpress.webapp.client.services.verifiedvendorservice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

import java.util.List;

/**
 * A service the retrieve all VendorFeedbacks for a give user
 * Created by michael on 11/14/2013.
 */
public interface VerifiedVendorServiceAsync {
    void removeVerifiedVendor(Long verifiedVendorId, AsyncCallback callback)
            throws IllegalArgumentException;
    void getVerifiedVendors(String userId, AsyncCallback<List<VerifiedVendor>> callback);
}
