package com.sneakyxpress.webapp.client.services.friendsservice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.User;

import java.util.List;

/**
 * A service the retrieve all VendorFeedbacks for a give user
 * Created by michael on 11/14/2013.
 */
public interface FriendsServiceAsync {
    void getUserFriends(String userId, List<String> friendsIds, AsyncCallback<List<User>> callback)
            throws IllegalArgumentException;
}
