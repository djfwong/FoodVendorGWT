package com.sneakyxpress.webapp.client.services.favouritesservice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.List;

/**
 * A service the retrieve all VendorFeedbacks for a give user
 * Created by michael on 11/14/2013.
 */
public interface FavouritesServiceAsync {
    void getFavourites(String userId, AsyncCallback<List<Favourite>> callback)
            throws IllegalArgumentException;
}
