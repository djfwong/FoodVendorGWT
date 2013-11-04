package com.sneakyxpress.webapp.shared;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@PersistenceCapable
public class VendorFeedback implements IsSerializable {
	/**
	 * VendorFeedback domain object
	 */

	// Unique identifier for object
	@PrimaryKey
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Persistent
	private Long id;

	// User Id
	@Column(name = "userId")
	@Persistent
	private String userId;

	// Vendor Id
	@Column(name = "vendorId")
	@Persistent
	private String vendorId;

	// Deals
	@Column(name = "rating")
	@Persistent
	private int rating;

	// Hours of operation
	@Column(name = "review")
	@Persistent
	private String review;
	
	public void setId(Long id){
		this.id = id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public void setVendorId(String vendorId){
		this.vendorId = vendorId;
	}
	
	public void setRating(int rating){
		this.rating = rating;
	}
	
	public void setReview(String review){
		this.review = review;
	}
	
	public Long getId(){
		return id;
	}
	
	public String getUserId(){
		return userId;
	}
	
	public String getvendorId(){
		return vendorId;
	}
	
	public int getRating(){
		return rating;
	}
	
	public String getReview(){
		return review;
	}

}