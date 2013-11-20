package com.sneakyxpress.webapp.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.pages.browsevendors.BrowseVendorsContent;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.pages.greeting.GreetingContent;
import com.sneakyxpress.webapp.client.pages.profile.ProfileContent;
import com.sneakyxpress.webapp.client.pages.search.SearchContent;
import com.sneakyxpress.webapp.client.pages.truckclaim.TruckClaimContent;
import com.sneakyxpress.webapp.client.services.updatedata.UpdateDataService;
import com.sneakyxpress.webapp.client.services.updatedata.UpdateDataServiceAsync;
import com.sneakyxpress.webapp.client.pages.viewvendor.ViewVendorContent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sneaky_Xpress implements EntryPoint {
	private final Logger logger = Logger.getLogger("");
    private final RootPanel loading = RootPanel.get("loading");

    public final FacebookTools FACEBOOK_TOOLS = new FacebookTools(this);

	// The pages to be shown in the navigation bar
	public final Content HOME_PAGE = new GreetingContent(this);
    public final Content SEARCH_PAGE = new SearchContent(this);
    public final Content VENDOR_PAGE = new ViewVendorContent(this);
    public final Content BROWSE_PAGE = new BrowseVendorsContent(this);
    public final Content PROFILE_PAGE = new ProfileContent(this);
    public final Content TRUCK_CLAIM_PAGE = new TruckClaimContent(this);

    // Register all pages here so they can be iterated over (used for History support)
    private final Content[] ALL_PAGES = { HOME_PAGE, SEARCH_PAGE, VENDOR_PAGE, BROWSE_PAGE, PROFILE_PAGE, TRUCK_CLAIM_PAGE };

    // A panel to hold messages in
    private FlowPanel messages = new FlowPanel();


    /**
     * Changes the contents of the page to the specified Content
     *
     * @param content   the content to change the page to
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
     *
     * @param error     is this message an error (red) or not (blue)?
     * @param info      the information to be displayed in the message
     */
    public void addMessage(boolean error, String info) {
        // Create & add the message
        String type = error ? "alert-danger" : "alert-info";
        HTML alert = new HTML("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">"
                + "&times;</button>" + info);
        alert.addStyleName(type + " alert fade in");
        messages.insert(alert, 0); // Add the message to the top

        // Animations & logging
        loading.addStyleName("collapsed"); // Remove the loading bar if it exists
        logger.log(Level.INFO, "addMessage: " + info);
    }


    /**
     * Creates a pop-up message in the centre of the page
     *
     * @param title     The title of the modal
     * @param body      The content of the modal
     */
    public Modal addModal(String title, Widget body) {
        Modal modal = new Modal(true, true);
        modal.setTitle(title);
        modal.add(body);
        modal.show();
        return modal;
    }


	/**
	 * This is the entry point method
	 */
	public void onModuleLoad() {
        addBrandName(); // Create the brand link in the navigation bar

        addNavigationLinks(); // Add page & function links to the navigation bar

        addSearchForm(); // Add the search form to the navigation bar

        RootPanel.get("messages").add(messages); // Add the messages panel

        initializeHistory(); // Add history support

        History.fireCurrentHistoryState(); // Load the current page, by default the home page
    }


    /**
     * Adds the large brand name to the navigation bar. The text links to our home page.
     */
    private void addBrandName() {
        Anchor brandLink = new Anchor(HOME_PAGE.getPageName());
        brandLink.addClickHandler(new PageClickHandler(HOME_PAGE, ""));
        brandLink.addStyleName("brand");
        RootPanel.get("brand").add(brandLink);
        logger.log(Level.INFO, "addBrandName: created the brand name link");
    }


    /**
     * Adds the navigation links to the navigation bar. This includes page links and other functional links (such as
     * login etc.)
     */
    private void addNavigationLinks() {
        RootPanel navbarList = RootPanel.get("navigation-bar");

        // Add the browse vendors link
        HTMLPanel browseListElement = new HTMLPanel("li", "");
        Anchor browseLink = new Anchor(BROWSE_PAGE.getPageName());
        browseLink.addClickHandler(new PageClickHandler(BROWSE_PAGE, "list"));
        browseListElement.add(browseLink);
        navbarList.add(browseListElement);
        logger.log(Level.INFO, "addNavigationLinks: created the browse link");

        // Add the login link
        HTMLPanel loginListElement = new HTMLPanel("li", "");
        loginListElement.add(FACEBOOK_TOOLS.getLoginLink());
        navbarList.add(loginListElement);
        logger.log(Level.INFO, "addNavigationLinks: created the login link");

        // Add the update data link
        HTMLPanel dataListElement = new HTMLPanel("li", "");
        dataListElement.add(getDataLink());
        navbarList.add(dataListElement);
        logger.log(Level.INFO, "addNavigationLinks: created the update data link");
    }


    /**
     * Adds the search form to the navigation bar
     */
    private void addSearchForm() {
        // Create the search form
        final TextBox searchInput = TextBox.wrap(Document.get().getElementById("search-input"));
        searchInput.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    String input = searchInput.getText().trim(); // Remove extra whitespace

                    if (!input.matches("(\\w| )*")) {
                        addMessage(true, "Sorry, \"" + input + "\" contains invalid characters. "
                                + "Only letters and spaces are allowed. Please remove them and try again.");
                    } else if (input.length() > 20) {
                        addMessage(true, "Sorry, your search query is too long. " +
                                "Please limit your query to 20 characters.");
                    } else if (input.length() == 0) {
                        addMessage(true, "Sorry your search query is empty. Please enter something.");
                    } else {
                        History.newItem(SEARCH_PAGE.getPageStub() + "?" + input);
                    }
                }
            }
        });
        logger.log(Level.INFO, "addSearchForm: created the search form");
    }


    /**
     * Adds history support to our application. History manages the forward/back functions of the application and also
     * which page to load depending on the url.
     */
    private void initializeHistory() {
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

                logger.log(Level.INFO, "History: pageStub=\"" + pageStub + "\" input=\"" + input + "\"");

                // Parse the history token
                for (Content page : ALL_PAGES) {
                    if (page.getPageStub().equals(pageStub)) {
                        page.getAndChangeContent(input);
                        return;
                    }
                }

                // By default load the home page
                HOME_PAGE.getAndChangeContent("");
            }
        });
        logger.log(Level.INFO, "initializeHistory: added history support");
    }


    /**
     * Creates the link used to update food vendor data. This is
     * used for debugging (the release has a button specifically for
     * administrators).
     *
     * @return      the link
     */
    private Anchor getDataLink() {
        Anchor link = new Anchor("Update Data");
        link.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loading.removeStyleName("collapsed");

                UpdateDataServiceAsync updateDataService = GWT
                        .create(UpdateDataService.class);
                updateDataService.updateData(new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        loading.addStyleName("collapsed");
                        addMessage(true, "Updating data failed. Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        loading.addStyleName("collapsed");
                        addModal("Data Updated", new HTML(response));
                    }
                });
            }
        });

        return link;
    }

    /**
     * Creates the button used to update food vendor data
     *
     * @return      the button
     */
    public Button getUpdateDataButton() {
        Button button = new Button("Update Data");
        button.addStyleName("btn btn-large btn-info");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loading.removeStyleName("collapsed");

                UpdateDataServiceAsync updateDataService = GWT
                        .create(UpdateDataService.class);
                updateDataService.updateData(new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        loading.addStyleName("collapsed");
                        addMessage(true, "Updating data failed. Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        loading.addStyleName("collapsed");
                        addModal("Data Updated", new HTML(response));
                    }
                });
            }
        });

        return button;
    }
}
