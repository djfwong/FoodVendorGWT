package com.sneakyxpress.webapp.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.BrowseVendorsService;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The server side implementation of the RPC service.
 */
public class BrowseVendorsServiceImpl extends RemoteServiceServlet implements
        BrowseVendorsService {

    public List<FoodVendor> browseVendorsServer(String input) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery();
        q.setClass(FoodVendor.class);

        List<FoodVendor> results = (List<FoodVendor>) q.execute();
        return new ArrayList<FoodVendor>(results);
    }
}
