package com.sneakyxpress.webapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.profile.ProfileService;
import com.sneakyxpress.webapp.shared.User;

/**
 * Retrieves data to use to create a user's profile page
 */
public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {
    @Override
    public User getUserInfo(String userId) throws IllegalArgumentException {
        return null;
    }
}
