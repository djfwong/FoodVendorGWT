package com.sneakyxpress.webapp.client.services.favouritesservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.List;

/**
 * The client side stub for retrieving VendorFeedback
 * Created by michael on 11/14/2013.
 */
@RemoteServiceRelativePath("getFavourites")
public interface FavouritesService extends RemoteService {
    List<Favourite> getFavourites(String userId) throws IllegalArgumentException;
}
