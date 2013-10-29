package com.sneakyxpress.webapp.client.comparators;

import java.util.Comparator;

import com.sneakyxpress.webapp.shared.FoodVendor;

public class LocComparator implements Comparator<FoodVendor> {
	public int compare(FoodVendor o1, FoodVendor o2) {
        if (o1 == o2) {
            return 0;
        }

        // Compare the location columns
        if (o1 == null || o1.getLocation().isEmpty()) {
            return 1;
        } else if (o2 == null || o2.getLocation().isEmpty()) {
            return -1;
        } else {
            return o1.getLocation().compareTo(o2.getLocation());
        }
	}
}
