package com.sneakyxpress.webapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.friendsservice.FriendsService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.User;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/14/2013.
 */
public class FriendsServiceImpl extends RemoteServiceServlet
        implements FriendsService {

    @Override
    public List<User> getUserFriends(String userId, List<String> friendsIds)
            throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<User> friends = new ArrayList<User>();

        // Get friends who use the app
        Query q = pm.newQuery("SELECT FROM " + User.class.getName());
        List<User> allUsers = (List<User>) q.execute();

        for (User u : allUsers) {
            for (String id : friendsIds) {
                if (u.getId().equals(id)) {
                    friends.add(u);
                }
            }
        }

        // Clean up
        pm.close();

        if (friends.isEmpty()) {
            return null;
        } else {
            return friends;
        }
    }
}
