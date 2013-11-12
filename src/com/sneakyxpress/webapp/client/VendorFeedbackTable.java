package com.sneakyxpress.webapp.client;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.sneakyxpress.webapp.shared.VendorFeedback;

public class VendorFeedbackTable {

	private List<VendorFeedback> vendorFeedbackList;
	
	public VendorFeedbackTable(List<VendorFeedback> reviews) {
		vendorFeedbackList = reviews;
	}
	
	public Widget getWidget(){
		
		// Create new cell table object
		CellTable<VendorFeedback> table = new CellTable<VendorFeedback>();

		// Configure table to display all results onto one page
		table.setPageSize(vendorFeedbackList.size());
		
		// Add Review column
		TextColumn<VendorFeedback> reviewCol = new TextColumn<VendorFeedback>() {
			@Override
			public String getValue(VendorFeedback vendorFeedback) {
				return vendorFeedback.getReview();
			}
		};
		table.addColumn(reviewCol, "Reviews");
		
		// Add Rating column 
		TextColumn<VendorFeedback> ratingCol = new TextColumn<VendorFeedback>() {
			@Override
			public String getValue(VendorFeedback vendorFeedback) {
				return Integer.toString(vendorFeedback.getRating());
			}
		};
		table.addColumn(ratingCol, "Ratings");
	
	// Create a list data provider.
	final ListDataProvider<VendorFeedback> dataProvider = new ListDataProvider<VendorFeedback>();
	// Add the cell table to the dataProvider.
	dataProvider.addDataDisplay(table);

	// List to add results to for data provider
	List<VendorFeedback> list = dataProvider.getList();

	for (VendorFeedback vf : vendorFeedbackList) {
		list.add(vf);
	}
	
	// return table and display on page
	return table;
	}
}
