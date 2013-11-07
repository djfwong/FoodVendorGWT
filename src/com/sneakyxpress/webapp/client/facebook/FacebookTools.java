package com.sneakyxpress.webapp.client.facebook;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.sneakyxpress.webapp.client.PageClickHandler;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.shared.User;

/**
 * Contains various functions to help with Facebook integration and OAuth login
 */
public class FacebookTools {
	
	private final PersistUserServiceAsync persistService = GWT.create(PersistUserService.class);
	
	private static final Logger logger = Logger.getLogger(""); // Our logger
	public static Sneaky_Xpress module;

	// Use the implementation of Auth intended to be used in the GWT client app.
	private static final Auth AUTH = Auth.get();
	private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";

	// This app's personal client ID assigned by the Facebook Developer App
	// (http://www.facebook.com/developers).
	private static final String FACEBOOK_APP_ID = "383766345086697"; // 181220262080855
																		// (Michael's)
	// All available scopes are listed here:
	// http://developers.facebook.com/docs/authentication/permissions/
	// This scope allows the app to access the user's email address.
	private static final String FACEBOOK_EMAIL_SCOPE = "email";

	// This scope allows the app to access the user's birthday.
	private static final String FACEBOOK_BIRTHDAY_SCOPE = "user_birthday";

	// The url to make graph requests to
	private static final String FACEBOOK_GRAPH_URL = "https://graph.facebook.com";

	// Our oath token. If we don't have one, it's an empty string.
	private static String token = "";

    // Keeps track of whether or not we are logged in
    private boolean loggedIn = false;

	// Data retrieved from facebook, by default null
	private List<String> userFriendsIds = new LinkedList<String>();
	private String userId = "<em class=\"muted\">No ID Available</em>";
	private String userName = "<em class=\"muted\">No Name Available</em>";

    private Anchor loginLink;
    private HandlerRegistration clickHandlerReg;

	/**
	 * Constructor for FacebookTools
	 * 
	 * @param module    The module that is using this tool set
	 */
	public FacebookTools(Sneaky_Xpress module) {
		this.module = module;
	}


	public String getUserId() throws NotLoggedInException {
		if (!loggedIn) {
            throw new NotLoggedInException();
        } else {
            return userId;
        }
	}


	public String getUserName() throws NotLoggedInException {
		if (!loggedIn) {
            throw new NotLoggedInException();
        } else {
            return userName;
        }
	}


	public List<String> getUserFriendsIds() throws NotLoggedInException {
		if (!loggedIn) {
            throw new NotLoggedInException();
        } else {
            return userFriendsIds;
        }
	}


	/**
	 * Returns an anchor that, when clicked, retrieves an OAuth token from
	 * Facebook and saves the token in this utility class.
	 * 
	 * @return          The login anchor
	 */
	public Anchor getLoginLink() {
		// Since the auth flow requires opening a popup window, it must be
		// started
		// as a direct result of a user action, such as clicking a button or
		// link.
		// Otherwise, a browser's popup blocker may block the popup.
		loginLink = new Anchor("Login");
		clickHandlerReg = loginLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loginAndUpdate();
            }
        });

		return loginLink;
	}


    /**
     * Change the login link into a profile link. The user must be logged in.
     */
    private void convertLoginLink() {
        clickHandlerReg.removeHandler(); // Remove the login action from the link

        // Change the text and action to be for the profile page
        loginLink.setText("My Profile");
        loginLink.addClickHandler(new PageClickHandler(module.PROFILE_PAGE, userId));
    }


	/**
	 * If the user is not logged in, displays a window so the user can log in.
	 * If the user accepts this login, we get an access token that we save.
	 * 
	 * Once we have the access token, we use it to retrieve the user's
	 * information.
	 */
	private void loginAndUpdate() {
		// Facebook expects a comma-delimited list of scopes
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL,
				FACEBOOK_APP_ID).withScopes(FACEBOOK_EMAIL_SCOPE,
				FACEBOOK_BIRTHDAY_SCOPE).withScopeDelimiter(",");

		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				logger.log(Level.INFO, "Got facebook oauth login token");
				FacebookTools.token = token;

				// Update the user's information
				retrieveUserInfo();
				retrieveUserFriends();
			}

			@Override
			public void onFailure(Throwable caught) {
				String message = "Facebook login failed. Reason: "
						+ caught.getMessage();
				logger.log(Level.SEVERE, message);
				module.addMessage(message);
			}
		});
	}


	private void retrieveUserInfo() {
		String url = FACEBOOK_GRAPH_URL + "/me?access_token=" + token;

		JsonpRequestBuilder builder = new JsonpRequestBuilder();
		builder.requestObject(url, new AsyncCallback<JavaScriptObject>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE,
						"Retrieving user data from facebook failed. Reason: " + caught.getMessage());
			}

			@Override
			public void onSuccess(JavaScriptObject jsObj) {
				logger.log(Level.INFO,
						"Successfully retrieved user data from facebook");
				JSONObject userInfo = new JSONObject(jsObj);

				// Grab user details and make new user object from JSON object
				User user = new User();

				if (userInfo.containsKey("email")) {
					user.setEmail(userInfo.get("email").toString());
					logger.log(Level.INFO, "Parsed user email: " + userInfo.get("email").toString());
				}

				if (userInfo.containsKey("id")) {
					userId = userInfo.get("id").toString();
					user.setId(userId);
					logger.log(Level.INFO, "Parsed user ID: " + userId);
				}

				persistNewUser(user);
			}
		});
	}


	private void retrieveUserFriends() {
		// TODO: Implement
	}


	// Method to add user to data store
	private void persistNewUser(final User user) {
		persistService.persistNewUserToDatastore(user,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
                        module.addMessage("Could not log in. Reason: " + caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
                        convertLoginLink();

						logger.log(Level.INFO, "Persist user result: " + result);
                        if (result) {
                            module.addMessage("Welcome new member. Enjoy your stay!");
					    } else {
                            module.addMessage("Welcome back " + user.getEmail());
                        }

                        retrieveUserFriends(); // Get the user's friends
                    }
				});
	}


	/**
	 * Returns an anchor that, when clicked, retrieves an OAuth token from
	 * Facebook and saves the token in this utility class.
	 * 
	 * @return      The login anchor
	 */
	public Anchor getLogoutLink() {
		// Clear token
		Anchor link = new Anchor("Logout");
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Auth.get().clearAllTokens();
		        Window.alert("All tokens cleared");
			}
		});
		return link;
	}
}
