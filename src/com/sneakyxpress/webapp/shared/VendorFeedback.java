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
	private int id;

	@Persistent
	private String authorUserId;

	@Persistent
	private String ownerVendorId;

	@Persistent
	private int rating;

	@Persistent
	private String review = "";

    @Persistent
    private Date createDate;

    public VendorFeedback() {
        createDate = new Date();
    }


	public void setId(int id){
		this.id = id;
	}


	public void setAuthorUserId(String authorUserId){
		this.authorUserId = authorUserId;
	}


	public void setOwnerVendorId(String vendorId){
		this.ownerVendorId = ownerVendorId;
	}


	public void setRating(int rating){
		this.rating = rating;
	}


	public void setReview(String review){
		this.review = review;
	}


	public int getId(){
		return id;
	}


	public String getAuthorUserId(){
		return authorUserId;
	}


	public String getownerVendorId(){
		return ownerVendorId;
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