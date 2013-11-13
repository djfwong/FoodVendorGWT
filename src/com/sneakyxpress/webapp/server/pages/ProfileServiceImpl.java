package com.sneakyxpress.webapp.server.pages;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.sneakyxpress.webapp.client.pages.profile.ProfileService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.User;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


/**
 * Retrieves data to use to create a user's profile page
 */
public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {
    @Override
    public User getUserInfo(String userId) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery("SELECT UNIQUE FROM " + User.class.getName()
                + " WHERE id == \"" + userId + "\"");
        User result = (User) q.execute();

        if (result == null) { // Not sure if this works
            throw new IllegalArgumentException("No user with an ID of " + userId + " could be found!");
        } else {
            return result;
        }
    }
}
