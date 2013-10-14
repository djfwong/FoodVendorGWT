package com.sneakyxpress.webapp.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FoodVendor {

	/**
	 * FoodVendor domain object
	 */

	// Unique identifier for object
	@Id
	@Column(name = "key")
	private String vendorId;

	// Business name
	@Column(name = "name")
	private String name;

	// Location
	@Column(name = "location")
	private String location;

	// Description
	@Column(name = "description")
	private String description;

	// Longitude coordinate
	@Column(name = "longitude")
	private double longitude;

	// Latitude coordinate
	@Column(name = "latitude")
	private double latitude;
}