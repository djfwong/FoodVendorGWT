package com.sneakyxpress.webapp.client.customwidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesServiceAsync;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * Created by michael on 11/20/2013.
 */
public class FavouriteButton extends Composite {
    private final Sneaky_Xpress module;

    private final Button starButton;

    private final String id;
    private final String vendorId;
    private final String userId;
    private final String vendorName;

    private boolean isFavourite = false;

    private final FavouritesServiceAsync favouritesService = GWT.
            create(FavouritesService.class);

    public FavouriteButton(final Sneaky_Xpress module, String vendorId, String vendorName, String userId) {
        this.module = module;
        this.id = vendorId + userId;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.userId = userId;

        // Create the button
        starButton = new Button("<i class=\"icon-heart\"></i>");
        starButton.addStyleName("btn");
        initWidget(starButton);

        // Set the initial colour and status
        favouritesService.getFavourite(id, new AsyncCallback<Favourite>() {
            @Override
            public void onFailure(Throwable caught) {
                module.addMessage(true, "Error loading favourite status. Reason: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Favourite result) {
                if (result != null) {
                    isFavourite = true;
                    updateButtonColour();
                }
            }
        });

        // Add a click handler
        starButton.addClickHandler(new StarButtonClickHandler());
    }

    public FavouriteButton(Sneaky_Xpress module, FoodVendor v, String userId) {
        this(module, v.getVendorId(), v.getName(), userId);
    }

    private void updateButtonColour() {
        if (isFavourite) {
            starButton.addStyleName("btn-success");
        } else {
            starButton.removeStyleName("btn-success");
        }
    }

    private class StarButtonClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            if (isFavourite) {
                // Remove as favourite
                favouritesService.removeFavourite(id, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, "Could not remove favourite. Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        isFavourite = false;
                        updateButtonColour();
                    }
                });
            } else {
                // Persist as favourite
                Favourite f = new Favourite();
                f.setVendorId(vendorId);
                f.setVendorName(vendorName);
                f.setUserId(userId);
                favouritesService.persistFavourite(f, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, "Could not add favourite. Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        isFavourite = true;
                        updateButtonColour();
                    }
                });
            }
        }
    }
}
