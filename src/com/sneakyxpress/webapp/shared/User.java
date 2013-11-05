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
	@PrimaryKey
	@Persistent
	@Id
	private String email;

	// First name
	@Column(name = "fname")
	@Persistent
	private String fname;

	// last name
	@Column(name = "lname")
	@Persistent
	private String lname;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	// gender
	@Column(name = "gender")
	@Persistent
	private String gender;
}