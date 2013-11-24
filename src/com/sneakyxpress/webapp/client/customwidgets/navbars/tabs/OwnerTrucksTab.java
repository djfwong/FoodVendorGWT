package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesService;
import com.sneakyxpress.webapp.client.services.favouritesservice.FavouritesServiceAsync;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorService;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorServiceAsync;
import com.sneakyxpress.webapp.shared.FormValidator;
import com.sneakyxpress.webapp.shared.User;
import com.sneakyxpress.webapp.shared.VendorFeedback;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

import java.util.List;

/**
 * Created by michael on 11/19/2013.
 */
public class OwnerTrucksTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;
    private final User user;

    public OwnerTrucksTab(Sneaky_Xpress module, User user) {
        this.module = module;
        this.user = user;
    }

    @Override
    public String getTitle() {
        return "My Trucks";
    }

    @Override
    public FlowPanel getContent() {
        final FlowPanel trucks = new FlowPanel();
        trucks.addStyleName("well");

        VerifiedVendorServiceAsync verifiedVendorService = GWT
                .create(VerifiedVendorService.class);
        verifiedVendorService.getVerifiedVendors(user.getId(),
                new AsyncCallback<List<VerifiedVendor>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(
                                true,
                                "Loading your truck failed! Reason: "
                                        + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<VerifiedVendor> result) {
                        if (result == null) {
                            HTMLPanel response = new HTMLPanel("p",
                                    "No trucks could be found :(");
                            response.addStyleName("lead pagination-centered");
                            trucks.add(response);
                        } else {
                            for (VerifiedVendor v : result) {
                                trucks.add(getVendorWidget(v));
                            }
                        }
                    }
                });

        return trucks;
    }

    private HTMLPanel getVendorWidget(final VerifiedVendor v) {
        HTMLPanel vendor = new HTMLPanel("p", v.getVendorId());
        vendor.addStyleName("lead");

        final HTMLPanel stats1 = new HTMLPanel("span", "");
        vendor.add(stats1);
        final HTMLPanel stats2 = new HTMLPanel("span", "");
        vendor.add(stats2);

        // Get the ratings for this truck
        VendorFeedbackServiceAsync vendorFeedbackService = GWT
                .create(VendorFeedbackService.class);
        vendorFeedbackService.getVendorReviews(v.getVendorId(),
                new AsyncCallback<List<VendorFeedback>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(
                                true,
                                "Could not load reviews for vendor "
                                        + v.getVendorName() + ". Reason: "
                                        + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<VendorFeedback> result) {
                        if (result != null) {
                            double mean = 0.0;
                            for (VendorFeedback f : result) {
                                mean += f.getRating();
                            }
                            mean /= result.size();
                            String formatted = NumberFormat.getFormat("0.0").format(mean);
                            stats1.add(new HTMLPanel("span", ", " + result.size() + " reviews, " + formatted
                                    + " average rating"));
                        }
                    }
                });

        // Get the number of favourites for this truck
        FavouritesServiceAsync favouritesService = GWT
                .create(FavouritesService.class);
        favouritesService.getNumVendorFavourites(v.getVendorId(),
                new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(
                                true,
                                "Failed to load favourites for vendor "
                                        + v.getVendorName() + ". Reason: "
                                        + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        stats2.add(new HTMLPanel("span", ", " + result
                                + " favourites"));
                    }
                });

        HTMLPanel buttonGroup = new HTMLPanel("span", "");
        buttonGroup.addStyleName("pull-right btn-group");

        buttonGroup.add(getViewButton(v));
        buttonGroup.add(getEditButton(v));
        buttonGroup.add(getDeleteButton(v));

        vendor.add(buttonGroup);

        return vendor;
    }

    private Anchor getViewButton(final VerifiedVendor v) {
        Anchor vendorLink = new Anchor("View");
        vendorLink.addStyleName("btn btn-info");
        vendorLink.addClickHandler(new PageClickHandler(module.VENDOR_PAGE, v
                .getVendorId()));
        return vendorLink;
    }

    private Anchor getEditButton(final VerifiedVendor v) {
        Anchor editLink = new Anchor("Edit");
        editLink.addStyleName("btn btn-warning");

        final TextBox phoneNo = new TextBox();
        phoneNo.addStyleName("input-block-level");

        final TextBox email = new TextBox();
        email.addStyleName("input-block-level");

        final TextArea hours = new TextArea();
        hours.addStyleName("input-block-level");

        final TextArea deals = new TextArea();
        deals.addStyleName("input-block-level");

        final Button submitButton = new Button("Submit Changes");
        submitButton.addStyleName("btn btn-info");

        // The modal
        final Modal[] modal = new Modal[1];

        editLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modal[0] = new Modal(true, true);
                modal[0].setTitle("Edit Vendor");

                // The form
                HTMLPanel form = new HTMLPanel("form", "");
                HTMLPanel fieldSet = new HTMLPanel("fieldset", "<label>Phone No.</label>");
                phoneNo.setText(v.getPhoneNumber());
                fieldSet.add(phoneNo);

                fieldSet.add(new HTML("<br>"));
                fieldSet.add(new HTML("<label>Email</label>"));
                email.setText(v.getEmail());
                fieldSet.add(email);

                fieldSet.add(new HTML("<br>"));
                fieldSet.add(new HTML("<label>Hours</label>"));
                hours.setText(v.getHours());
                fieldSet.add(hours);

                fieldSet.add(new HTML("<br>"));
                fieldSet.add(new HTML("<label>Deals</label>"));
                deals.setText(v.getDeals());
                fieldSet.add(deals);

                fieldSet.add(new HTML("<br>"));
                fieldSet.add(submitButton);

                form.add(fieldSet);
                modal[0].add(form);
                modal[0].show();
            }
        });

        // The submit button handler
        submitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                v.setPhoneNumber(phoneNo.getText());
                v.setEmail(email.getText());
                v.setHours(hours.getText());
                v.setDeals(deals.getText());

                if (!phoneNo.getText().isEmpty() && !FormValidator.validatePhoneNo(phoneNo.getText())) {
                    module.addMessage(true, "Please ensure your phone number contains 10 numbers and no invalid characters.");
                } else if (!email.getText().isEmpty() && !FormValidator.validateEmail(email.getText())) {
                    module.addMessage(true, "Please enter a valid email address.");
                } else if (!hours.getText().isEmpty() && FormValidator.containsIllegal(hours.getText())) {
                    module.addMessage(true, "Please remove any invalid characters from the hours text box.");
                } else if (!deals.getText().isEmpty() && FormValidator.containsIllegal(deals.getText())) {
                    module.addMessage(true, "Please remove any invalid characters from the hours text box.");
                } else {
                    VerifiedVendorServiceAsync verifiedVendorService = GWT
                            .create(VerifiedVendorService.class);
                    verifiedVendorService.addVerifiedVendor(v, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            module.addMessage(true, "Updating vendor failed. Reason: " + caught.getMessage());
                            modal[0].hide();
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            module.addMessage(false, "Updating vendor was successful!");
                            modal[0].hide();
                        }
                    });
                }
            }
        });

        return editLink;
    }

    private Anchor getDeleteButton(final VerifiedVendor v) {
        final Anchor deleteLink = new Anchor("Delete");
        deleteLink.addStyleName("btn btn-danger");

        deleteLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                HTMLPanel areYouSure = new HTMLPanel(
                        "<p class=\"lead pagination-centered\">"
                                + "Are you sure?</p>");
                Button confirm = new Button("Confirm Deletion");
                confirm.addStyleName("btn btn-danger btn-large btn-block");

                areYouSure.add(confirm);
                final Modal modal = module.addModal("Confirm Deletion", areYouSure);

                confirm.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        VerifiedVendorServiceAsync verifiedVendorService = GWT
                                .create(VerifiedVendorService.class);
                        verifiedVendorService.removeVerifiedVendor(v.getVendorId(),
                                new AsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        module.addMessage(
                                                true,
                                                "Deleting failed! Reason: "
                                                        + caught.getMessage());
                                        modal.hide();
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        if (result) {
                                            module.addMessage(
                                                    false,
                                                    v.getVendorId()
                                                            + " has been removed from your account.");
                                            deleteLink.getParent().getParent()
                                                    .removeFromParent(); // May need to
                                            // go
                                            // higher!
                                        } else {
                                            module.addMessage(true,
                                                    "Error occurred with your request, please try again later.");
                                        }
                                        modal.hide();

                                    }
                                });
                    }
                });
            }
        });

        return deleteLink;
    }
}
