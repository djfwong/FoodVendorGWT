package com.sneakyxpress.webapp.client.facebook;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;

/**
 * Contains various functions to help with Facebook integration and OAuth login
 */
public class FacebookTools {
    private static final Logger logger = Logger.getLogger(""); // Our logger
    public static Sneaky_Xpress module;

    // Use the implementation of Auth intended to be used in the GWT client app.
    private static final Auth AUTH = Auth.get();
    private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";

    // This app's personal client ID assigned by the Facebook Developer App
    // (http://www.facebook.com/developers).
    private static final String FACEBOOK_CLIENT_ID = "383766345086697";

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
    JSONObject userInfo = null;

    /**
     * Constructor for FacebookTools
     * @param module    The module that is using this tool set
     */
    public FacebookTools(Sneaky_Xpress module) {
        this.module = module;
    }

    public JSONObject getUserInfo() throws NotLoggedInException {
        if (userInfo == null) {
            throw new NotLoggedInException();
        }

        return userInfo;
    }

    public JSONObject getFriends() {
        return null;
    }

    /**
     * Returns an anchor that, when clicked, retrieves an OAuth token from Facebook and
     * saves the token in this utility class.
     *
     * @return      The login anchor
     */
    public Anchor getLoginLink() {
        // Since the auth flow requires opening a popup window, it must be started
        // as a direct result of a user action, such as clicking a button or link.
        // Otherwise, a browser's popup blocker may block the popup.
        Anchor link = new Anchor("Login");
        link.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                login();
            }
        });

        return link;
    }

    /**
     * If the user is not logged in, displays a window so the user can log in.
     * If the user accepts this login, we get an access token that we save.
     *
     * Once we have the access token, we use it to retrieve the user's information.
     */
    private void login() {
        final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL, FACEBOOK_CLIENT_ID)
                .withScopes(FACEBOOK_EMAIL_SCOPE, FACEBOOK_BIRTHDAY_SCOPE)
                .withScopeDelimiter(","); // Facebook expects a comma-delimited list of scopes

        AUTH.login(req, new Callback<String, Throwable>() {
            @Override
            public void onSuccess(String token) {
                logger.log(Level.INFO, "Got facebook oauth login token");
                FacebookTools.token = token;

                retrieveUserInfo();
            }

            @Override
            public void onFailure(Throwable caught) {
                String message = "Facebook login failed. Reason: " + caught.getMessage();
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
                logger.log(Level.SEVERE, "Retrieving user data from facebook failed. Reason: " + caught.getMessage());
            }

            @Override
            public void onSuccess(JavaScriptObject jsObj) {
                logger.log(Level.INFO, "Successfully retrieved user data from facebook");
                userInfo = new JSONObject(jsObj);
              
                
                if (userInfo.containsKey("email")){
                	Window.alert(userInfo.get("email").toString());
                }
                if (userInfo.containsKey("first_name")){
                	Window.alert(userInfo.get("first_name").toString());
                }
                if (userInfo.containsKey("last_name")){
                	Window.alert(userInfo.get("last_name").toString());
                }
                if (userInfo.containsKey("gender")){
                	Window.alert(userInfo.get("gender").toString());
                }
           
            }
        });
    }
}
