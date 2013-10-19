package com.sneakyxpress.webapp.server;

import java.util.List;

public class TestParser{
	public static void main(String[] args){
		
		//PersistData data1 = new PersistData();
		
		String csvFile = "/Users/djfwong/git/Sneaky_Xpress/Files/new_food_vendor_locations2.csv";
		CSVParser parser = new CSVParser();
		
		List<String> s = parser.importCSVFileRowsToList(csvFile);
		List<FoodVendor> v = parser.parseExcelRowStrings(s);

		//data1.persistData(v);	
	}
	
	
}