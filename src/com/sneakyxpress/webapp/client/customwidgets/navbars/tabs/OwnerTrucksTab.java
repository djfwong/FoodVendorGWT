package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.sneakyxpress.webapp.shared.User;
import com.sneakyxpress.webapp.shared.VendorFeedback;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

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
	public String getTitle()
	{
		return "My Trucks";
	}

	@Override
	public FlowPanel getContent()
	{
		final FlowPanel trucks = new FlowPanel();
		trucks.addStyleName("well");

		VerifiedVendorServiceAsync verifiedVendorService = GWT
				.create(VerifiedVendorService.class);
		verifiedVendorService.getVerifiedVendors(user.getId(),
				new AsyncCallback<List<VerifiedVendor>>() {
					@Override
					public void onFailure(Throwable caught)
					{
						module.addMessage(
								true,
								"Loading your truck failed! Reason: "
										+ caught.getMessage());
					}

					@Override
					public void onSuccess(List<VerifiedVendor> result)
					{
						if (result == null)
						{
							HTMLPanel response = new HTMLPanel("p",
									"No trucks could be found :(");
							response.addStyleName("lead pagination-centered");
							trucks.add(response);
						}
						else
						{
							for (VerifiedVendor v : result)
							{
								trucks.add(getVendorWidget(v));
							}
						}
					}
				});

		return trucks;
	}

	private HTMLPanel getVendorWidget(final VerifiedVendor v)
	{
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
					public void onFailure(Throwable caught)
					{
						module.addMessage(
								true,
								"Could not load reviews for vendor "
										+ v.getVendorName() + ". Reason: "
										+ caught.getMessage());
					}

					@Override
					public void onSuccess(List<VendorFeedback> result)
					{
						if (result != null)
						{
							double mean = 0.0;
							for (VendorFeedback f : result)
							{
								mean += f.getRating();
							}
							mean /= result.size();
                            stats1.add(new HTMLPanel("span", ", " + result.size() + " reviews, " + mean
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
					public void onFailure(Throwable caught)
					{
						module.addMessage(
								true,
								"Failed to load favourites for vendor "
										+ v.getVendorName() + ". Reason: "
										+ caught.getMessage());
					}

					@Override
					public void onSuccess(Integer result)
					{
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

	private Anchor getViewButton(VerifiedVendor v)
	{
		Anchor vendorLink = new Anchor("View");
		vendorLink.addStyleName("btn btn-info");
		vendorLink.addClickHandler(new PageClickHandler(module.VENDOR_PAGE, v
				.getVendorId()));
		return vendorLink;
	}

	private Anchor getEditButton(final VerifiedVendor v)
	{
		Anchor editLink = new Anchor("Edit");
		editLink.addStyleName("btn btn-warning");

        editLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                HTMLPanel form = new HTMLPanel("form", "");
                HTMLPanel fieldSet = new HTMLPanel("fieldset", "<label>Phone No.</label>");

                final TextArea phoneNo = new TextArea();
                phoneNo.addStyleName("input-block-level");
                phoneNo.setText(v.getPhoneNumber());
                fieldSet.add(phoneNo);

                fieldSet.add(new HTML("<br>"));
                fieldSet.add(new HTML("<label>Hours</label>"));

                final TextArea hours = new TextArea();
                hours.addStyleName("input-block-level");
                hours.setText(v.getHours());
                fieldSet.add(hours);

                fieldSet.add(new HTML("<br>"));
                fieldSet.add(new HTML("<label>Deals</label>"));

                final TextArea deals = new TextArea();
                deals.addStyleName("input-block-level");
                deals.setText(v.getDeals());
                fieldSet.add(deals);

                fieldSet.add(new HTML("<br>"));

                Button submitButton = new Button("Submit Changes");

                fieldSet.add(submitButton);
                form.add(fieldSet);
                final Modal modal = module.addModal("Edit Vendor", form);

                submitButton.addStyleName("btn btn-info");
                submitButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        v.setPhoneNumber(phoneNo.getText());
                        v.setHours(hours.getText());
                        v.setDeals(deals.getText());

                        VerifiedVendorServiceAsync verifiedVendorService = GWT
                                .create(VerifiedVendorService.class);
                        verifiedVendorService.addVerifiedVendor(v, new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                module.addMessage(true, "Updating vendor failed. Reason: " + caught.getMessage());
                                modal.hide();
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                module.addMessage(false, "Updating vendor was successful!");
                                modal.hide();
                            }
                        });
                    }
                });
            }
        });

		return editLink;
	}

	private Anchor getDeleteButton(final VerifiedVendor v)
	{
		final Anchor deleteLink = new Anchor("Delete");
		deleteLink.addStyleName("btn btn-danger");

		deleteLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event)
			{
				VerifiedVendorServiceAsync verifiedVendorService = GWT
						.create(VerifiedVendorService.class);
				verifiedVendorService.removeVerifiedVendor(v.getVendorId(),
						new AsyncCallback<Boolean>() {
							@Override
							public void onFailure(Throwable caught)
							{
								module.addMessage(
										true,
										"Deleting failed! Reason: "
												+ caught.getMessage());
							}

							@Override
							public void onSuccess(Boolean result)
							{
								if (result)
								{
									module.addMessage(
											false,
											v.getVendorId()
													+ " has been removed from your account.");
									deleteLink.getParent().getParent()
											.removeFromParent(); // May need to
																	// go
																	// higher!
								}
								else
								{
									module.addMessage(true,
											"Error occurred with your request, please try again later.");
								}

							}
						});
			}
		});

		return deleteLink;
	}
}
