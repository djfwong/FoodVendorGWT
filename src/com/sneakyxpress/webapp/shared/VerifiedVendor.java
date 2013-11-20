package com.sneakyxpress.webapp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VerifiedVendor implements IsSerializable {
	// Unique identifier for object
	@Id
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

    @Persistent
	private String userId;

    @Persistent
	private String vendorId;

    @Persistent
    private String vendorName;

    @Persistent
	private String deals;

    @Persistent
	private String hours;

    @Persistent
	private String phoneNumber;

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

    public String getDeals() {
        return deals;
    }

    public void setDeals(String deals) {
        this.deals = deals;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Long getId() {
        return id;
    }
}