package com.sneakyxpress.webapp.client;

import java.util.Comparator;

import com.sneakyxpress.webapp.shared.FoodVendor;

public class NameComparator implements Comparator<FoodVendor> {
	public int compare(FoodVendor o1, FoodVendor o2) {
		if (o1 == o2) {
			return 0;
		}

		// Compare the title columns.
		if (o1 != null) {
			return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
		}
		return -1;
	}
}