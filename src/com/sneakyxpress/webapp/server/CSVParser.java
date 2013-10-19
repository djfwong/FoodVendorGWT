package com.sneakyxpress.webapp.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

	String csvFile = "/Users/djfwong/git/Sneaky_Xpress/Files/new_food_vendor_locations2.csv";

	public List<String> importCSVFileRowsToList(String path) {

		List<String> excelRow = new ArrayList<String>();

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));

			String line = null;

			// remove first line of csv file
			br.readLine();

			while ((line = br.readLine()) != null) {
				System.out.println(line);

				excelRow.add(line);
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return excelRow;
	}

	public List<FoodVendor> parseExcelRowStrings(List<String> list) {

		List<FoodVendor> vendors = new ArrayList<FoodVendor>();

		for (String s : list) {

			String[] foodVendor = s.split(",");

			FoodVendor vendor = new FoodVendor();

			vendor.setVendorId(foodVendor[0]);
			// Ignore columns 1, 2
			String str1 = foodVendor[1];
			String str2 = foodVendor[2];
			vendor.setName(foodVendor[3]);
			vendor.setLocation(foodVendor[4]);
			vendor.setDescription(foodVendor[5]);
			vendor.setLatitude(Double.parseDouble(foodVendor[6]));
			vendor.setLongitude(Double.parseDouble(foodVendor[7]));

			vendors.add(vendor);
		}

		return vendors;
	}

}