package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimService;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimServiceAsync;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserService;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserServiceAsync;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorService;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorServiceAsync;
import com.sneakyxpress.webapp.shared.TruckClaim;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

public class ClaimsTab extends AbstractNavbarTab {

	private final ClaimServiceAsync claimsService = GWT
			.create(ClaimService.class);

	private final PersistUserServiceAsync userService = GWT
			.create(PersistUserService.class);

	private final VerifiedVendorServiceAsync verifiedVendorService = GWT
			.create(VerifiedVendorService.class);

	protected static final Logger logger = Logger.getLogger("");

	private Sneaky_Xpress module;

	private String fbId;
	private String truckId;
	private Long claimId;

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
			public void onFailure(Throwable caught)
			{
				System.out.println("Exception: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<TruckClaim> result)
			{

				if (!result.isEmpty())
				{

					// Create table
					CellTable<TruckClaim> table = new CellTable<TruckClaim>();

					// Configure table to display all results onto one page
					table.setPageSize(result.size());

					// Add Truck Id column
					TextColumn<TruckClaim> truckIdCol = new TextColumn<TruckClaim>() {
						@Override
						public String getValue(TruckClaim claim)
						{
							return claim.getTruckId();
						}
					};
					table.addColumn(truckIdCol, "Truck ID");

					// Add Facebook Id column
					TextColumn<TruckClaim> fbIdCol = new TextColumn<TruckClaim>() {
						@Override
						public String getValue(TruckClaim claim)
						{
							return claim.getFacebookId();
						}
					};
					table.addColumn(fbIdCol, "Facebook ID");

					// Add Name column
					TextColumn<TruckClaim> nameCol = new TextColumn<TruckClaim>() {
						@Override
						public String getValue(TruckClaim claim)
						{
							return claim.getName();
						}
					};
					table.addColumn(nameCol, "Name");

					// Add Email column
					TextColumn<TruckClaim> emailCol = new TextColumn<TruckClaim>() {
						@Override
						public String getValue(TruckClaim claim)
						{
							return claim.getEmail();
						}
					};
					table.addColumn(emailCol, "Email");

					// Add Phone Number column
					TextColumn<TruckClaim> phoneCol = new TextColumn<TruckClaim>() {
						@Override
						public String getValue(TruckClaim claim)
						{
							return claim.getPhoneNumber();
						}
					};
					table.addColumn(phoneCol, "Phone No.");

					// Add Accepted/Rejected column
					TextColumn<TruckClaim> isAcceptedCol = new TextColumn<TruckClaim>() {
						@Override
						public String getValue(TruckClaim claim)
						{
							return String.valueOf(claim.isAccepted());
						}
					};
					table.addColumn(isAcceptedCol, "Accepted");

					// Add Accepted/Rejected column
					TextColumn<TruckClaim> isViewedCol = new TextColumn<TruckClaim>() {
						@Override
						public String getValue(TruckClaim claim)
						{
							return String.valueOf(claim.isViewed());
						}
					};
					table.addColumn(isViewedCol, "Viewed");

					// Create a list data provider.
					final ListDataProvider<TruckClaim> dataProvider = new ListDataProvider<TruckClaim>();
					// Add the cell table to the dataProvider.
					dataProvider.addDataDisplay(table);

					// List to add results to for data provider
					List<TruckClaim> list = dataProvider.getList();

					for (TruckClaim tc : result)
					{
						list.add(tc);
					}
					clickTableRow(table);
					claimsPanel.add(table);
				}
			}
		});

		return claimsPanel;
	}

	public void clickTableRow(CellTable<TruckClaim> table)
	{
		// Add a selection model to handle user selection
		final SingleSelectionModel<TruckClaim> selectionModel = new SingleSelectionModel<TruckClaim>();
		table.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event)
					{
						TruckClaim selected = selectionModel
								.getSelectedObject();
						if (selected != null)
						{
							fbId = selected.getFacebookId();
							truckId = selected.getTruckId();
							claimId = selected.getId();
							module.addModal("Claim ID: " + selected.getId(),
									claimAcceptanceWidget());
						}

					}
				});
	}

	public Widget claimAcceptanceWidget()
	{

		HTMLPanel content = new HTMLPanel("");
		HTMLPanel panel = new HTMLPanel("");

		// Accept Claim button
		Button acceptButton = new Button("Accept");
		acceptButton.addStyleName("btn btn-success");
		acceptButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				claimsService.acceptClaim(claimId,
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
										"claimsService.acceptClaim: " + result);

								if (result)
								{
									module.addMessage(false, "Claim accepted.");
									updateUserRole(fbId);
									addToVerifiedVendors(fbId, truckId);

								}
								else
								{
									module.addMessage(true,
											"Error in accepting claim.");
								}
							}

						});
			}
		});

		panel.add(getButtonWidget(acceptButton));
		content.add(panel);

		Button rejectButton = new Button("Reject");
		rejectButton.addStyleName("btn btn-danger");
		rejectButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
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
			}
		});

		panel.add(getButtonWidget(rejectButton));
		content.add(panel);
		return content;
	}

	// Creates button with space in between panels
	private HTMLPanel getButtonWidget(Button button)
	{
		HTMLPanel div = new HTMLPanel("<br>");
		div.add(button);
		return div;
	}

	// Change role of user to owner
	public void updateUserRole(String id)
	{
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
	public void addToVerifiedVendors(String userId, String tId)
	{
		VerifiedVendor v = new VerifiedVendor();
		v.setId(userId);
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
						if (result)
						{
							module.addMessage(false,
									"Updated Truck As Verified");
						}
						else
						{
							module.addMessage(true,
									"Error in verifying truck, could be already claimed");
						}
					}

				});

	}
}