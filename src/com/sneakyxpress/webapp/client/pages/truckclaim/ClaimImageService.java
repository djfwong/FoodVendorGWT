package com.sneakyxpress.webapp.client.pages.truckclaim;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("images")
public interface ClaimImageService extends RemoteService {
	public String getBlobstoreUploadUrl();
}