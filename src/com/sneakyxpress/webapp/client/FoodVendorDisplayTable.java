package com.sneakyxpress.webapp.client;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
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

				// Configure to select a row vs one cell at a time
				MultiSelectionModel<FoodVendor> selectionModel = new MultiSelectionModel<FoodVendor>();
				table.setSelectionModel(selectionModel);

				// Create a list data provider.
				final ListDataProvider<FoodVendor> dataProvider = new ListDataProvider<FoodVendor>();

				// Instantiate sort handler
				ListHandler<FoodVendor> colSortHandler = new ListHandler<FoodVendor>(
						vendorList);

				// Add Key column and sort to column
				TextColumn<FoodVendor> keyCol = new TextColumn<FoodVendor>() {
					@Override
					public String getValue(FoodVendor vendor) {
						return vendor.getVendorId();
					}
				};
				table.addColumn(keyCol, "Key");
				keyCol.setSortable(true);
				colSortHandler.setComparator(keyCol, new KeyComparator());
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
				colSortHandler.setComparator(nameCol, new NameComparator());

				// Add Location column
				TextColumn<FoodVendor> locCol = new TextColumn<FoodVendor>() {
					@Override
					public String getValue(FoodVendor vendor) {
						return vendor.getLocation();
					}
				};
				table.addColumn(locCol, "Location");
				locCol.setSortable(true);
				colSortHandler.setComparator(locCol, new LocComparator());

				// Add Description column
				TextColumn<FoodVendor> desCol = new TextColumn<FoodVendor>() {
					@Override
					public String getValue(FoodVendor vendor) {
						return vendor.getDescription();
					}
				};
				table.addColumn(desCol, "Description");
				desCol.setSortable(true);
				colSortHandler.setComparator(desCol, new DesComparator());

				// Add the cell table to the dataProvider.
				dataProvider.addDataDisplay(table);

				// Set to query result
				dataProvider.setList(vendorList);
				
				// Add col sort handler to table
				table.addColumnSortHandler(colSortHandler);
				table.getColumnSortList().push(keyCol);

				// return table and display on page
				return table;
	}
}
