package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.FavouriteButton;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesServiceAsync;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.FoodVendor;
import com.sneakyxpress.webapp.shared.User;

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
        favouritesService.getUserFavourites(user.getId(),
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
                            header.addStyleName("lead pagination-centered");
                            favourites.add(header);

                            for (Favourite f : result) {
                                favourites.add(getVendorWidget(f));
                            }
                        }
                    }
                });

        return favourites;
    }

    private HTMLPanel getVendorWidget(Favourite f) {
        String name = f.getVendorName();
        if (name.isEmpty()) {
            name = "<em class=\"muted\">No Name Available</em>";
        }

        HTMLPanel favourite = new HTMLPanel("p", name + " ");
        favourite.addStyleName("lead");

        // Group of buttons
        HTMLPanel buttonGroup = new HTMLPanel("");
        buttonGroup.addStyleName("pull-right btn-group");

        // The heart button
        buttonGroup.add(
                new FavouriteButton(module, f.getVendorId(), f.getVendorName(), f.getUserId()));

        // The view button
        Button vendorLink = new Button("View");
        vendorLink.addClickHandler(new PageClickHandler(module.VENDOR_PAGE, f.getVendorId()));
        vendorLink.addStyleName("btn btn-info");
        buttonGroup.add(vendorLink);

        favourite.add(buttonGroup);

        return favourite;
    }
}
