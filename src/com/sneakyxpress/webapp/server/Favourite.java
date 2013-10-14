package com.sneakyxpress.webapp.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favourite {
	/**
	 * Favourite domain object
	 */

	// Unique identifier for object
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// User Id
	@Column(name = "userId")
	private String userId;

	// Vendor Id
	@Column(name = "vendorId")
	private String vendorId;

}