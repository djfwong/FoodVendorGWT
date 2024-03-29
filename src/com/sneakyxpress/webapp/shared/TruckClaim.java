package com.sneakyxpress.webapp.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class TruckClaim implements IsSerializable {

	// Since users can have multiple trucks, or multiple claim forms for one
	// truck can be submitted, will use auto-generated PK instead
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String facebookId = "";

	@Persistent
	private String truckId = "";

	@Persistent
	private String name = "";

	@Persistent
	private String email = "";

	@Persistent
	private String phoneNumber = "";

	@Persistent
	private boolean isViewed = false;

	@Persistent
	private boolean isAccepted = false;

	public Long getId()
	{
		return id;
	}

	public String getFacebookId()
	{
		return facebookId;
	}

	public void setFacebookId(String facebookId)
	{
		this.facebookId = facebookId;
	}

	public String getTruckId()
	{
		return truckId;
	}

	public void setTruckId(String truckId)
	{
		this.truckId = truckId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public boolean isAccepted()
	{
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted)
	{
		this.isAccepted = isAccepted;
	}

	public boolean isViewed()
	{
		return isViewed;
	}

	public void setViewed(boolean isViewed)
	{
		this.isViewed = isViewed;
	}

}
