package com.sneakyxpress.webapp.client;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.sneakyxpress.webapp.shared.FoodVendor;

public class FoodVendorDisplayTable {

	private List<FoodVendor> vendorList;

	public FoodVendorDisplayTable(List<FoodVendor> vendors) {
		vendorList = vendors;
	}

	public Widget getWidget() {

		// Create new cell table object
		CellTable<FoodVendor> table = new CellTable<FoodVendor>();

		// Configure table to display all results onto one page
		table.setPageSize(vendorList.size());
		
		// Add Key column and sort to column
		TextColumn<FoodVendor> keyCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getVendorId();
			}
		};
		table.addColumn(keyCol, "Key");
		keyCol.setSortable(true);
		keyCol.setDefaultSortAscending(true);

		// Add Name column and sort to column
		TextColumn<FoodVendor> nameCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getName();
			}
		};
		table.addColumn(nameCol, "Name");
		nameCol.setSortable(true);

		// Add Location column
		TextColumn<FoodVendor> locCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getLocation();
			}
		};
		table.addColumn(locCol, "Location");
		locCol.setSortable(true);

		// Add Description column
		TextColumn<FoodVendor> desCol = new TextColumn<FoodVendor>() {
			@Override
			public String getValue(FoodVendor vendor) {
				return vendor.getDescription();
			}
		};
		desCol.setSortable(true);
		table.addColumn(desCol, "Description");

		// Create a list data provider.
		final ListDataProvider<FoodVendor> dataProvider = new ListDataProvider<FoodVendor>();
		// Add the cell table to the dataProvider.
		dataProvider.addDataDisplay(table);

		// List to add results to for data provider 
		List<FoodVendor> list = dataProvider.getList();

		for (FoodVendor fd : vendorList) {
			list.add(fd);
		}

		// Instantiate sort handler
		ListHandler<FoodVendor> colSortHandler = new ListHandler<FoodVendor>(
				list);

		// Add the comparators for each column to the column sort handler
		colSortHandler.setComparator(desCol, new DesComparator());
		colSortHandler.setComparator(keyCol, new KeyComparator());
		colSortHandler.setComparator(nameCol, new NameComparator());
		colSortHandler.setComparator(locCol, new LocComparator());

		// Add column sort handler to table
		table.addColumnSortHandler(colSortHandler);

		// Set to query result
		table.getColumnSortList().push(keyCol);

		// return table and display on page
		return table;
	}
}
