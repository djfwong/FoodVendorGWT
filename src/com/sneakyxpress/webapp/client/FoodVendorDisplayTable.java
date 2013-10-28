package com.sneakyxpress.webapp.client;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.sneakyxpress.webapp.shared.FoodVendor;

public class FoodVendorDisplayTable {
	
	private List<FoodVendor> vendorList;
	
	public FoodVendorDisplayTable(
			List<FoodVendor> vendors)
	{
		vendorList = vendors;
	}
	
	public Widget getWidget() {
		// Create new cell table object
		CellTable<FoodVendor> table = new CellTable<FoodVendor>();

		// Configure table to display all results onto one page
		table.setPageSize(vendorList.size());
        table.addStyleName("table table-striped");

		// Add Key column
		TextColumn<FoodVendor> keyCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getVendorId();
			}
		};
        table.addColumn(keyCol, "Key");

		// Add Name column
		TextColumn<FoodVendor> nameCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getName();
			}
		};
		table.addColumn(nameCol, "Name");

		// Add Location column
		TextColumn<FoodVendor> locCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getLocation();
			}
		};
		table.addColumn(locCol, "Location");

		// Add Description column
		TextColumn<FoodVendor> desCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getDescription();
			}
		};
		table.addColumn(desCol, "Description");

		// Create a list data provider.
		final ListDataProvider<FoodVendor> dataProvider = new ListDataProvider<FoodVendor>();

		// Add the cellList to the dataProvider.
		dataProvider.addDataDisplay(table);

		// Set to query result
		dataProvider.setList(vendorList);

		// return table and display on page
		return table;
	}

}
