package com.sneakyxpress.webapp.client.services.friendsservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.User;

import java.util.List;

/**
 * The client side stub for retrieving VendorFeedback
 * Created by michael on 11/14/2013.
 */
@RemoteServiceRelativePath("getUserFriends")
public interface FriendsService extends RemoteService {
    List<User> getUserFriends(String userId, List<String> friendsIds) throws IllegalArgumentException;
}
