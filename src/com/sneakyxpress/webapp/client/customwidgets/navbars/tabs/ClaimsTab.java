package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.pages.greeting.GreetingContent;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimService;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimServiceAsync;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserService;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserServiceAsync;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorService;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorServiceAsync;
import com.sneakyxpress.webapp.shared.TruckClaim;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClaimsTab extends AbstractNavbarTab {

	private final ClaimServiceAsync claimsService = GWT
			.create(ClaimService.class);

	private final PersistUserServiceAsync userService = GWT
			.create(PersistUserService.class);

	private final VerifiedVendorServiceAsync verifiedVendorService = GWT
			.create(VerifiedVendorService.class);

	protected static final Logger logger = Logger.getLogger("");

	private Modal modal;
	private Sneaky_Xpress module;

	public ClaimsTab(Sneaky_Xpress module) {
		this.module = module;
	}

	@Override
	public String getTitle()
	{
		return "Truck Claims";
	}

	public FlowPanel getContent()
	{
		final FlowPanel claimsPanel = new FlowPanel();

		claimsService.retrieveClaims(new AsyncCallback<List<TruckClaim>>() {

			@Override
			public void onFailure(Throwable caught) {
                module.addMessage(true, "Failed to load claim. Reason: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<TruckClaim> result) {

				if (!result.isEmpty()) {
                    SimpleTable claimsTable = new SimpleTable("table-hover table-bordered",
                            "Truck ID", "Facebook ID", "Name", "Email", "Phone No.", "Accepted", "Viewed");

                    for (TruckClaim c : result) {
                        claimsTable.addRow(new ClaimClickHandler(c.getId(), c.getFacebookId(), c.getTruckId()),
                                c.getTruckId(), c.getFacebookId(), c.getName(), c.getEmail(), c.getPhoneNumber(),
                                String.valueOf(c.isAccepted()), String.valueOf(c.isViewed()));
                    }

                    claimsTable.sortRows(0, false);

                    claimsPanel.add(claimsTable);
				}
			}
		});

		return claimsPanel;
	}

    private class ClaimClickHandler implements ClickHandler {
        private final Long claimId;
        private final String fbId;
        private final String truckId;

        public ClaimClickHandler(Long claimId, String fbId, String truckId) {
            this.claimId = claimId;
            this.fbId = fbId;
            this.truckId = truckId;
        }

        @Override
        public void onClick(ClickEvent event) {
            modal = module.addModal("Claim ID: " + claimId,
                    claimAcceptanceWidget(claimId, fbId, truckId));
        }
    }

	public Widget claimAcceptanceWidget(final Long claimId, final String fbId, final String truckId) {

		HTMLPanel content = new HTMLPanel("");

		// Accept Claim button
		Button acceptButton = new Button("Accept");
		acceptButton.addStyleName("btn btn-success btn-large btn-block");
		acceptButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                modal.hide();
                claimsService.acceptClaim(claimId,
                        new AsyncCallback<Boolean>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                logger.log(Level.SEVERE, caught.getMessage());
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                logger.log(Level.INFO,
                                        "claimsService.acceptClaim: " + result);

                                if (result) {
                                    module.addMessage(false, "Claim accepted.");

                                    updateUserRole(fbId);

                                    addToVerifiedVendors(fbId, truckId);

                                } else {
                                    module.addMessage(true,
                                            "Error in accepting claim.");
                                }
                            }

                        });
                History.newItem(new GreetingContent(module).getPageStub());
            }
        });

		Button rejectButton = new Button("Reject");
		rejectButton.addStyleName("btn btn-danger btn-large btn-block");
		rejectButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				modal.hide();
				claimsService.rejectClaim(claimId,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught)
							{
								logger.log(Level.SEVERE, caught.getMessage());
							}

							@Override
							public void onSuccess(Boolean result)
							{
								logger.log(Level.INFO,
										"claimsService.rejectClaim: " + result);

								if (result)
								{
									module.addMessage(false, "Claim rejected.");
								}
								else
								{
									module.addMessage(true,
											"Error in rejecting claim.");
								}
							}
						});

				History.newItem(new GreetingContent(module).getPageStub());
			}
		});

        content.add(acceptButton);
        content.add(rejectButton);
		return content;
	}

	// Change role of user to owner
	public void updateUserRole(String id) {

		userService.changeUserStatus(id, 2, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught)
			{
				logger.log(Level.SEVERE, caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result)
			{
				logger.log(Level.INFO, "userService.changeUserStatus: "
						+ result);

				if (result)
				{
					module.addMessage(false, "Updated User Role");
				}
				else
				{
					module.addMessage(true, "Error in updating user role.");
				}
			}

		});

	}

	// Update VerifiedVendor table
	public void addToVerifiedVendors(String userId, String tId) {
		VerifiedVendor v = new VerifiedVendor();
		v.setUserId(userId);
		v.setVendorId(tId);

		verifiedVendorService.addVerifiedVendor(v,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught)
					{
						logger.log(Level.SEVERE, caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result)
					{
						module.addMessage(false,
									"Updated Truck As Verified");
					}

				});

	}
}