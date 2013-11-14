package com.sneakyxpress.webapp.shared;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;


@Entity
@PersistenceCapable
public class VendorFeedback implements IsSerializable {
	@PrimaryKey
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Persistent
	private Long id;

	@Persistent
	private String userId;

	@Persistent
	private String vendorId;

	@Persistent
	private int rating;

	@Persistent
	private String review = "";

    @Persistent
    private Date createDate;

    public VendorFeedback() {
        createDate = new Date();
    }


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

    public long getCreationTime() {
        return createDate.getTime();
    }
}