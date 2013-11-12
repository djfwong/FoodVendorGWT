package com.sneakyxpress.webapp.client.updatedata;

import com.google.gwt.user.client.rpc.AsyncCallback;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by michael on 11/7/2013.
 */
public interface UpdateDataServiceAsync {
    void updateData(AsyncCallback<String> callback);
}
