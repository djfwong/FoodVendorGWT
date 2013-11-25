package com.sneakyxpress.webapp.shared;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Favourite implements IsSerializable {
	/**
	 * Favourite domain object
	 */

	// Unique identifier for object
	@Id
	@Persistent
	@PrimaryKey
	private String id;

	// User Id
	@Persistent
	private String userId;

	// Vendor Id
	@Persistent
	private String vendorId;

	@Persistent
	private String vendorName;

	@Persistent
	// Vendor description
	private String vendorDesc;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getVendorName()
	{
		return vendorName;
	}

	public void setVendorName(String vendorName)
	{
		this.vendorName = vendorName;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getVendorDesc()
	{
		return vendorDesc;
	}

	public void setVendorDesc(String vendorDesc)
	{
		this.vendorDesc = vendorDesc;
	}
}
