package com.sneakyxpress.webapp.client.pages.truckclaim;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.TruckClaim;

public interface ClaimServiceAsync {

	void retrieveClaims(AsyncCallback<List<TruckClaim>> callback);

	void acceptClaim(long id, AsyncCallback<Boolean> callback);

	void rejectClaim(Long claimId, AsyncCallback<Boolean> asyncCallback);

}
