package com.sneakyxpress.webapp.client.profile;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.User;

/**
 * A service to retrieve a user's information
 */
@RemoteServiceRelativePath("profile")
public interface ProfileService extends RemoteService {
    User getUserInfo(String userId) throws IllegalArgumentException;
}
