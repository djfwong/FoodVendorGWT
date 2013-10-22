package com.sneakyxpress.webapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>SearchService</code>.
 */
public interface SearchServiceAsync {
    void searchServer(String input, AsyncCallback<String> callback)
            throws IllegalArgumentException;
}
