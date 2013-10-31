package com.sneakyxpress.webapp.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sneakyxpress.webapp.client.browsevendors.BrowseVendorsContent;
import com.sneakyxpress.webapp.client.greeting.GreetingContent;
import com.sneakyxpress.webapp.client.search.SearchContent;
import com.sneakyxpress.webapp.client.viewvendor.ViewVendorContent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sneaky_Xpress implements EntryPoint {
	// Our logger
	private static Logger logger = Logger.getLogger("");
	private static RootPanel loading = RootPanel.get("loading");

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

	/**
	 * The pages to be shown in the navigation bar
	 */
	private final Content HOME_PAGE = new GreetingContent(this);
	private final Content SEARCH_PAGE = new SearchContent(this);
	private final Content VENDOR_PAGE = new ViewVendorContent(this);
	private final Content[] PAGES = new Content[] { new BrowseVendorsContent(
			this) };

    /**
     * Changes the contents of the page to the specified Content
     */
    public void changeContent(Widget content) {
        RootPanel body = RootPanel.get("content");
        body.clear();
        body.add(content);

        Window.scrollTo(0, 0); // Scroll to the top of the page
        loading.addStyleName("collapsed"); // Remove the loading bar
    }

    /**
     * Adds a message to the top of the page (user can close it)
     */
    public void addMessage(String info) {
        HTML alert = new HTML("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">"
                + "&times;</button><i class=\"icon-exclamation-sign\"></i> <strong>Warning!</strong> " + info);
        alert.addStyleName("alert alert-danger fade in");
        RootPanel.get("messages").add(alert);

        loading.addStyleName("collapsed"); // Remove the loading bar if it exists

        logger.log(Level.INFO, "addMessage: " + info);
    }

	/**
	 * This is the entry point method
	 */
	public void onModuleLoad() {

		addFacebookAuth();
		// Export the JS method that can be called in pure JS
	    Auth.export();

		// Create the brand/title in the navigation bar
		Anchor brandLink = new Anchor(HOME_PAGE.getPageName());
		brandLink.addClickHandler(new PageClickHandler(HOME_PAGE, ""));
		brandLink.addStyleName("brand");
		RootPanel.get("brand").add(brandLink);
		logger.log(Level.INFO, "onModuleLoad: created brand/title");

		// Create the navigation bar
		RootPanel navbarList = RootPanel.get("navigation-bar");
		for (Content page : PAGES) {
			Anchor pageLink = new Anchor(page.getPageName());

			pageLink.addClickHandler(new PageClickHandler(page, ""));

			HTMLPanel listElement = new HTMLPanel("li", "");
			listElement.add(pageLink);
			navbarList.add(listElement);
		}
		logger.log(Level.INFO, "onModuleLoad: created navigation bar");

		// Create the search form
		final TextBox searchInput = TextBox.wrap(Document.get().getElementById(
				"search-input"));
		searchInput.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String input = searchInput.getText().trim();

					if (!input.matches("(\\w| )*")) {
						addMessage("Sorry, \""
								+ input
								+ "\" contains invalid characters. "
								+ "Only letters and spaces are allowed. Please remove them and try again.");
					} else if (input.length() > 20) {
						addMessage("Sorry, your search query is too long. Please limit your query to 20 characters.");
					} else if (input.length() == 0) {
						addMessage("Sorry your search query is empty. Please enter something.");
					} else {
						History.newItem(SEARCH_PAGE.getPageStub() + "?" + input);
					}
				}
			}
		});
		logger.log(Level.INFO, "onModuleLoad: created the search form");

		// Implements history support
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				loading.removeStyleName("collapsed"); // Add the loading bar

				String historyToken = event.getValue();
				String pageStub = historyToken;
				String input = "";
				if (historyToken.contains("?")) {
					int split = historyToken.lastIndexOf("?");
					pageStub = historyToken.substring(0, split);
					input = historyToken.substring(split + 1);
				}

				logger.log(Level.INFO, "History: pageStub=\"" + pageStub
						+ "\" input=\"" + input + "\"");

				// Parse the history token
				boolean foundPage = false;
				for (Content page : PAGES) {
					if (page.getPageStub().equals(pageStub)) {
						page.getAndChangeContent(input);
						foundPage = true;
						break;
					}
				}

				if (!foundPage) {
					if (SEARCH_PAGE.getPageStub().equals(pageStub)) {
						SEARCH_PAGE.getAndChangeContent(input);
					} else if (VENDOR_PAGE.getPageStub().equals(pageStub)) {
						VENDOR_PAGE.getAndChangeContent(input);
					} else { // By default load the home page
						HOME_PAGE.getAndChangeContent(input);
					}
				}
			}
		});
		logger.log(Level.INFO, "onModuleLoad: added history support");

		// Load the current page, by default the home page
		History.fireCurrentHistoryState();

	}

	/**
	 * Getter for the VENDOR_PAGE
	 * 
	 * @return VENDOR_PAGE
	 */
	public Content getVendorPage() {
		return VENDOR_PAGE;
	}

	private void addFacebookAuth() {
		// Since the auth flow requires opening a popup window, it must be
		// started
		// as a direct result of a user action, such as clicking a button or
		// link.
		// Otherwise, a browser's popup blocker may block the popup.
		Button button = new Button("Authenticate with Facebook");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL,
						FACEBOOK_CLIENT_ID).withScopes(FACEBOOK_EMAIL_SCOPE,
						FACEBOOK_BIRTHDAY_SCOPE)
				// Facebook expects a comma-delimited list of scopes
						.withScopeDelimiter(",");
				AUTH.login(req, new Callback<String, Throwable>() {
					@Override
					public void onSuccess(String token) {
						Window.alert("Got an OAuth token:\n" + token + "\n"
								+ "Token expires in " + AUTH.expiresIn(req)
								+ " ms\n");
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error:\n" + caught.getMessage());
					}
				});
			}
		});
		RootPanel.get().add(button);
	}

}
