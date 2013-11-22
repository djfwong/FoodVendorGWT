package com.sneakyxpress.webapp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Entity;
import javax.persistence.Id;

@PersistenceCapable
public class FoodVendor implements IsSerializable {

	/**
	 * FoodVendor domain object
	 */
	// Unique identifier for object
	@PrimaryKey
	@Persistent
	@Id
	private String vendorId;

	// Business name
    @Persistent
	private String name = "";

	// Location
    @Persistent
	private String location = "";

	// Description
    @Persistent
	private String description = "";

	// Longitude coordinate
    @Persistent
	private double longitude = 0;

	// Latitude coordinate
    @Persistent
	private double latitude = 0;

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getVendorId() {
		return vendorId;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getDescription() {
		return description;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
}