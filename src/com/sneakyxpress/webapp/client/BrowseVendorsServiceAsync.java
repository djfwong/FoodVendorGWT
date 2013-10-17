package com.sneakyxpress.webapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The async counterpart of <code>BrowseVendorsService</code>.
 */
public interface BrowseVendorsServiceAsync {
    void browseVendorsServer(String input, AsyncCallback<String> callback)
            throws IllegalArgumentException;
}
