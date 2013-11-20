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
    @Id
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long feedbackId;

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

    public Long getFeedbackId() {
        return feedbackId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}