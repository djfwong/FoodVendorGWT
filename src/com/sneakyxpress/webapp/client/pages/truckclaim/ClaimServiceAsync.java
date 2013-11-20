package com.sneakyxpress.webapp.client.pages.truckclaim;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.TruckClaim;

public interface ClaimServiceAsync {

	void retrieveClaims(AsyncCallback<List<TruckClaim>> callback);

}
