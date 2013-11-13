package com.sneakyxpress.webapp.client.services.updatedata;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Created by michael on 11/7/2013.
 */
@RemoteServiceRelativePath("updateData")
public interface UpdateDataService extends RemoteService {
    String updateData();
}
