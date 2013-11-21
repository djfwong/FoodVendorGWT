package com.sneakyxpress.webapp.client.pages.truckclaim;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.TruckClaim;

/**
 * A service to retrieve a user's information
 */
@RemoteServiceRelativePath("claims")
public interface ClaimService extends RemoteService {
	List<TruckClaim> retrieveClaims() throws IllegalArgumentException;

	boolean acceptClaim(long id);

	boolean rejectClaim(Long claimId);
}
