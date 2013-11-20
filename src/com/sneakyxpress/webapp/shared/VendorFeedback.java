package com.sneakyxpress.webapp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@PersistenceCapable
public class VendorFeedback implements IsSerializable {
    // The key of a vendor feedback is defined to be [User ID][Vendor ID]
    @Id
    @PrimaryKey
    @Persistent
    private String id;

    @Persistent
	private String authorId;

    @Persistent
    private String authorName;

	@Persistent
	private String vendorId;

    @Persistent
    private String vendorName;

	@Persistent
	private int rating;

	@Persistent
	private String review = "";

    @Persistent
    private long createTime;

    public VendorFeedback() {
        Date temp = new Date();
        createTime = temp.getTime();
    }

	public void setAuthorId(String authorId){
		this.authorId = authorId;
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

	public String getAuthorId(){
		return authorId;
	}


	public String getVendorId(){
		return vendorId;
	}

	public int getRating(){
		return rating;
	}


	public String getReview(){
		return review;
	}

    public void setCreationTime(long time) {
        createTime = time;
    }

    public long getCreationTime() {
        return createTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}