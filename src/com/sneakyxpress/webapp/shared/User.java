package com.sneakyxpress.webapp.shared;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@PersistenceCapable
public class User implements IsSerializable {

    public static final int ADMINISTRATOR = 1;
    public static final int OWNER = 2;
    public static final int USER = 3;

    // Facebook Id of User
    @PrimaryKey
    @Persistent
    @Id
    private String id;

    @Persistent
    private int type = USER;

	// Unique identifier for object, email
	@Persistent
	private String email = "";

    @Persistent
    private String name = "";


    /**
     * Retrieves the type of the user.
     *
     * @return      Possible types are User.ADMINISTRATOR,
     *              User.OWNER, or User.USER
     */
    public int getType() {
        return type;
    }


    /**
     * Sets the type of the user
     *
     * @param type  Possible types are User.ADMINISTRATOR,
     *              User.OWNER, or User.USER
     */
    public void setType(int type) {
        this.type = type;
    }


    /**
     * Retrieves the type of the user in a human-readable String
     *
     * @return      The type description of the user
     */
    public String getTypeName() {
        switch (type) {
            case ADMINISTRATOR:
                return "Administrator";
            case OWNER:
                return "Owner";
            case USER:
                return "User";
            default:
                return "Unknown (" + type + ")";
        }
    }


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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
