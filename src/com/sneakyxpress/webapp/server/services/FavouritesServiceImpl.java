package com.sneakyxpress.webapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by michael on 11/14/2013.
 */
public class FavouritesServiceImpl extends RemoteServiceServlet
        implements FavouritesService {

    @Override
    public List<Favourite> getFavourites(String userId)
            throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT FROM " + Favourite.class.getName()
                + " WHERE userId == \"" + userId + "\"");
        List<Favourite> result = (List<Favourite>) q.execute();

        pm.close();

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }
}
