package com.sneakyxpress.webapp.server.services;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimImageService;

public class ClaimImageServiceImpl extends RemoteServiceServlet implements ClaimImageService {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String getBlobstoreUploadUrl() {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        return blobstoreService.createUploadUrl("/claimFormReq", UploadOptions.Builder.withGoogleStorageBucketName("sneaky-xpress"));
    }
 
}