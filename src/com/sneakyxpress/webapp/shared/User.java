package com.sneakyxpress.webapp.shared;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.jdo.annotations.Extension;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@PersistenceCapable
public class User implements IsSerializable {

	/**
	 * User domain object
	 */

    // Facebook Id of User
    @PrimaryKey
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    @Id
    private String id;

	// Unique identifier for object, email
	@Column(name = "email")
	@Persistent
	private String email = "";

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
