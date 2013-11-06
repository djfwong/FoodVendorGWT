package com.sneakyxpress.webapp.client.facebook;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.User;

public interface PersistUserServiceAsync {

	void persistNewUserToDatastore(User user, AsyncCallback<Boolean> callback);

	void userInDatabase(String userId, AsyncCallback<Boolean> callback);


}
	