package com.sneakyxpress.webapp.client.pages.truckclaim;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClaimImageServiceAsync {

	void getBlobstoreUploadUrl(AsyncCallback<String> callback);

}
