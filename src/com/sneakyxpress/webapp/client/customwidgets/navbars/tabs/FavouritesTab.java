package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesServiceAsync;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.User;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by michael on 11/14/2013.
 */
public class FavouritesTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;
    private final User user;

    public FavouritesTab(Sneaky_Xpress module, User user) {
        this.module = module;
        this.user = user;
    }

    @Override
    public String getTitle() {
        return "My Favourites";
    }

    @Override
    public FlowPanel getContent() {
        final FlowPanel favourites = new FlowPanel();
        favourites.addStyleName("well");

        FavouritesServiceAsync favouritesService = GWT
                .create(FavouritesService.class);
        favouritesService.getFavourites(user.getId(),
                new AsyncCallback<List<Favourite>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, "Loading reviews failed! Reason: " +
                                caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<Favourite> result) {
                        if (result == null) {
                            HTMLPanel response = new HTMLPanel("p", "No favourites could be found :(");
                            response.addStyleName("lead pagination-centered");
                            favourites.add(response);
                        } else {
                            HTMLPanel header = new HTMLPanel("p", "You have " + result.size() + " favourites!");
                            header.addStyleName("lead");
                            favourites.add(header);

                            for (Favourite f : result) {
                                favourites.add(getFavouriteWidget(f));
                            }
                        }
                    }
                });

        return favourites;
    }

    private HTMLPanel getFavouriteWidget(Favourite f) {
        String name = f.getVendorName();
        if (name.isEmpty()) {
            name = "<em>No Name Available</em>";
        }

        HTMLPanel vendor = new HTMLPanel("p", name + " ");

        Anchor vendorLink = new Anchor("View");
        vendorLink.addClickHandler(new PageClickHandler(module.VENDOR_PAGE, f.getVendorId()));

        vendor.add(vendorLink);

        return vendor;
    }
}
