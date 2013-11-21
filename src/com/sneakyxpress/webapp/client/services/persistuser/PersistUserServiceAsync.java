package com.sneakyxpress.webapp.client.services.persistuser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.User;

import java.util.List;

public interface PersistUserServiceAsync {
    void persistNewUserToDatastore(User user, AsyncCallback<Boolean> async);

	void changeUserStatus(String fbId, int type, AsyncCallback<Boolean> callback);

    void getAllUsers(AsyncCallback<List<User>> async);
}
	