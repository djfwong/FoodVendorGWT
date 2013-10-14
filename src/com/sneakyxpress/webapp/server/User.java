package com.sneakyxpress.webapp.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	/**
	 * User domain object
	 */

	// Unique identifier for object
	@Id
	@Column(name = "userId")
	private String userId;

	// Address
	@Column(name = "address")
	private String address;

	// Email
	@Column(name = "email")
	private String email;
}