package com.sneakyxpress.webapp.client.pages.profile;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.User;

/**
 * The async counterpart of ProfileService
 */
public interface ProfileServiceAsync {
    void getUserInfo(String userId, AsyncCallback<User> callback) throws IllegalArgumentException;
}
