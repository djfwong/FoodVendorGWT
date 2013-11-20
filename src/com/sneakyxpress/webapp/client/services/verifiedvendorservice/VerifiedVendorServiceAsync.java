package com.sneakyxpress.webapp.client.services.verifiedvendorservice;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

/**
 * A service the retrieve all VendorFeedbacks for a give user Created by michael
 * on 11/14/2013.
 */
public interface VerifiedVendorServiceAsync {
	void removeVerifiedVendor(String verifiedVendorId, AsyncCallback callback)
			throws IllegalArgumentException;

	void getVerifiedVendors(String userId,
			AsyncCallback<List<VerifiedVendor>> callback);

	void addVerifiedVendor(VerifiedVendor v, AsyncCallback<Boolean> callback);
}
