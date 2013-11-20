package com.sneakyxpress.webapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/14/2013.
 */
public class FavouritesServiceImpl extends RemoteServiceServlet
        implements FavouritesService {

    @Override
    public void persistFavourite(Favourite f) throws IllegalArgumentException {
        // Create the ID of the favourite. This will be unique.
        f.setId(f.getVendorId() + f.getUserId());

        // Check to see if this is an existing favourite
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery("SELECT UNIQUE FROM " + Favourite.class.getName()
                + " WHERE id == \"" + f.getId() + "\"");
        Favourite result = (Favourite) q.execute();

        if (result == null) {
            pm.makePersistent(f);
        }

        pm.close();
    }

    @Override
    public void removeFavourite(String id) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery("SELECT UNIQUE FROM " + Favourite.class.getName()
                + " WHERE id == \"" + id + "\"");
        Favourite result = (Favourite) q.execute();

        if (result != null) {
            pm.deletePersistent(result);
        }

        pm.close();
    }

    @Override
    public Favourite getFavourite(String id) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery("SELECT UNIQUE FROM " + Favourite.class.getName()
                + " WHERE id == \"" + id + "\"");
        Favourite result = (Favourite) q.execute();
        pm.close();

        return result;
    }

    @Override
    public List<Favourite> getUserFavourites(String userId)
            throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT FROM " + Favourite.class.getName()
                + " WHERE userId == \"" + userId + "\"");
        List<Favourite> result = (List<Favourite>) q.execute();
        result = new ArrayList<Favourite>(result);

        pm.close();

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public List<Favourite> getFriendsFavourites(List<String> friendsIds) throws IllegalArgumentException {
        List<Favourite> friendsFavourites = new ArrayList<Favourite>();

        PersistenceManager pm = PMF.get().getPersistenceManager();
        for (String id : friendsIds) {
            Query q = pm.newQuery("SELECT FROM " + Favourite.class.getName()
                    + " WHERE userId == \"" + id + "\"");
            List<Favourite> result = (List<Favourite>) q.execute();
            friendsFavourites.addAll(result);
        }

        pm.close();

        if (friendsFavourites.isEmpty()) {
            return null;
        } else {
            return friendsFavourites;
        }
    }

    @Override
    public int getNumVendorFavourites(String vendorId) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query q = pm.newQuery("SELECT FROM " + Favourite.class.getName()
                + " WHERE vendorId == \"" + vendorId + "\"");
        List<Favourite> result = (List<Favourite>) q.execute();

        pm.close();

        return result.size();
    }
}
