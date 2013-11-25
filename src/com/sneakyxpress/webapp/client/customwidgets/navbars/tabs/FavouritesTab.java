package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.FavouriteButton;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesServiceAsync;
import com.sneakyxpress.webapp.shared.Favourite;
import com.sneakyxpress.webapp.shared.User;

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

		final HTMLPanel info = new HTMLPanel(
				"<p class=\"lead pagination-centered\">All your favourite food vendors are listed below! You may also remove any of your saved favourites.</p>");
		favourites.add(info);
        
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
                            favourites.addStyleName("well");
                        } else {
                            info.addStyleName("well");
                            HTMLPanel header = new HTMLPanel("p", "You have " + result.size() + " favourites!");
                            header.addStyleName("lead pagination-centered");
                            info.add(header);


                            SimpleTable table = new SimpleTable("table-hover table-bordered",
                                    "Key", "Name");
                            List<FavouriteButton> hearts = new LinkedList<FavouriteButton>();

                            for (Favourite f : result) {
                                table.addRow(new PageClickHandler(module.VENDOR_PAGE, f.getVendorId()),
                                        f.getVendorId(), f.getVendorName());
                                FavouriteButton fb = new FavouriteButton(module, f.getVendorId(), f.getVendorName(),
                                        FacebookTools.getUserId());
                                hearts.add(fb);
                            }

                            table.addWidgetColumn("", hearts);
                            table.sortRows(0, false);

                            favourites.add(table);
                        }
                    }
                });

        return favourites;
    }
}
