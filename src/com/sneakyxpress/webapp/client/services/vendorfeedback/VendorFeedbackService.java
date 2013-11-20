package com.sneakyxpress.webapp.client.services.vendorfeedback;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.List;

/**
 * The client side stub for retrieving VendorFeedback
 * Created by michael on 11/14/2013.
 */
@RemoteServiceRelativePath("getVendorFeedback")
public interface VendorFeedbackService extends RemoteService {
    void persistVendorFeedback(VendorFeedback f) throws IllegalArgumentException;
    List<VendorFeedback> getVendorReviews(String vendorId) throws IllegalArgumentException;
    List<VendorFeedback> getVendorOwnerFeedback(String userId) throws IllegalArgumentException;
    List<VendorFeedback> getUserReviews(String userId) throws IllegalArgumentException;
}
