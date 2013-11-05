package com.sneakyxpress.webapp.shared;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@PersistenceCapable
public class User implements IsSerializable {

	/**
	 * User domain object
	 */

	// Unique identifier for object, email
	@Column(name = "email")
	@Persistent
	@Id
	private String email = "";

	// Facebook Id of User
	@PrimaryKey
	@Persistent
	private String id;

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