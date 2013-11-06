package com.sneakyxpress.webapp.client.facebook;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.User;

public interface PersistUserServiceAsync {

	void persistNewUserToDatastore(User user, AsyncCallback<String> callback);

}
	