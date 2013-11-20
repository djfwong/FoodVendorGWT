package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.profile.ProfileContent;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimService;
import com.sneakyxpress.webapp.client.pages.truckclaim.ClaimServiceAsync;
import com.sneakyxpress.webapp.shared.TruckClaim;

public class ClaimsTab extends AbstractNavbarTab {

	private final ClaimServiceAsync claimsService = GWT
			.create(ClaimService.class);

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

					claimsPanel.add(table);
				}
			}
		});

		return claimsPanel;
	}



	public void clickTableRow(CellTable<TruckClaim> table) {
		// Add a selection model to handle user selection 
		final SingleSelectionModel<TruckClaim> selectionModel = new SingleSelectionModel<TruckClaim>();
		table.setSelectionModel(selectionModel);
		selectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				TruckClaim selected = selectionModel
						.getSelectedObject();
				if (selected != null) {
					History.newItem(new ProfileContent(module).getPageStub());
				}

			}
		});
	}
}