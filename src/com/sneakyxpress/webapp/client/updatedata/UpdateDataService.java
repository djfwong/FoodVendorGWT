package com.sneakyxpress.webapp.client.updatedata;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by michael on 11/7/2013.
 */
@RemoteServiceRelativePath("updateData")
public interface UpdateDataService extends RemoteService {
    String updateData();
}
