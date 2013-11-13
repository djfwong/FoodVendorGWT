package com.sneakyxpress.webapp.client.services.updatedata;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by michael on 11/7/2013.
 */
public interface UpdateDataServiceAsync {
    void updateData(AsyncCallback<String> callback);
}
