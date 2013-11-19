package com.sneakyxpress.webapp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favourite implements IsSerializable {
	/**
	 * Favourite domain object
	 */

	// Unique identifier for object
	@Id
    @Persistent
    @PrimaryKey
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// User Id
    @Persistent
	private String userId;

	// Vendor Id
    @Persistent
	private String vendorId;

    @Persistent
    private String vendorName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
