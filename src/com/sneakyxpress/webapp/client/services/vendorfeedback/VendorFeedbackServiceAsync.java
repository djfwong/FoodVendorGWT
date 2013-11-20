package com.sneakyxpress.webapp.client.services.vendorfeedback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.List;

/**
 * A service the retrieve all VendorFeedbacks for a give user
 * Created by michael on 11/14/2013.
 */
public interface VendorFeedbackServiceAsync {
    void getVendorOwnerFeedback(String userId, AsyncCallback<List<VendorFeedback>> callback)
            throws IllegalArgumentException;
    void getUserReviews(String userId, AsyncCallback<List<VendorFeedback>> callback)
            throws IllegalArgumentException;

    void getVendorReviews(String vendorId, AsyncCallback<List<VendorFeedback>> async);

    void persistVendorFeedback(VendorFeedback f, AsyncCallback<Void> async);

    void deleteVendorFeedback(Long feedbackId, AsyncCallback<Void> async);
}
