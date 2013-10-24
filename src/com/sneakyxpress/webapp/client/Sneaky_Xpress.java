package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sneaky_Xpress implements EntryPoint {
    // Our logger
    private static Logger logger = Logger.getLogger("");
    
    /**
     * The pages to be shown in the navigation bar
     */
    private final Content HOME_PAGE = new GreetingContent(this);
    private final Content SEARCH_PAGE = new SearchContent(this);
    private final Content[] PAGES = new Content[] {
        new BrowseVendorsContent(this)
    };

    /**
     * Changes the contents of the page to the specified Content
     */
    public void changeContent(Widget content) {
        RootPanel body = RootPanel.get("content");
        body.clear();
        body.add(content);
        RootPanel.get("loading").setVisible(false); // Remove the loading bar
    }
    

    /**
     * Adds a message to the top of the page (user can close it)
     */
    public void addMessage(String info) {
        RootPanel messages = RootPanel.get("messages");
        HTML alert = new HTML("<div class=\"alert alert-dismissable alert-danger fade in\">"
                + "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">"
                + "&times;</button><i class=\"icon-exclamation-sign\"></i> "
                + "<strong>Warning!</strong> " + info + "</div>");
        messages.add(alert);
        logger.log(Level.INFO, "addMessage: " + info);
    }

    /**
     * Clears all messages
     */
    private void clearMessages() {
        RootPanel.get("messages").clear();
    }

	/**
	 * This is the entry point method
	 */
	public void onModuleLoad() {
     
	    /**
         * Handles actions invoked by clicking links in the navigation bar
         */
        class NavbarClickHandler implements ClickHandler {
            Content page;
            public NavbarClickHandler(Content page) {
                super();
                this.page = page;
            }

            @Override
            public void onClick(ClickEvent event) {
                History.newItem(page.getPageStub());
            }
        }
        

        // Create the brand/title in the navigation bar
        Anchor brandLink = new Anchor(HOME_PAGE.getPageName());
        brandLink.addClickHandler(new NavbarClickHandler(HOME_PAGE));
        brandLink.addStyleName("brand");
        RootPanel.get("brand").add(brandLink);
        logger.log(Level.INFO, "onModuleLoad: created brand/title");

        // Create the navigation bar
        RootPanel navbarList = RootPanel.get("navigation-bar");
        for (Content page : PAGES) {
            Anchor pageLink = new Anchor(page.getPageName());

            pageLink.addClickHandler(new NavbarClickHandler(page));

            HTMLPanel listElement = new HTMLPanel("li", "");
            listElement.add(pageLink);
            navbarList.add(listElement);
        }
        logger.log(Level.INFO, "onModuleLoad: created navigation bar");

        // Create the search form
        final TextBox searchInput = TextBox.wrap(Document.get().getElementById("search-input"));
        searchInput.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                String input = searchInput.getText().trim();
                if (!input.matches("(\\w| )*")) {
                    addMessage("Sorry, \"" + input + "\" contains invalid characters. "
                            + "Please remove them and try again.");
                } else if (input.length() > 20) {
                    addMessage("Sorry, your search query is too long. Please limit your query to 20 characters.");
                } else if (input.length() == 0) {
                    addMessage("Sorry your search query is empty. Please enter something.");
                } else {
                    History.newItem(SEARCH_PAGE.getPageStub() + "?" + searchInput.getText());
                }
            }
        });
        logger.log(Level.INFO, "onModuleLoad: created the search form");

        // Implements history support
        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                RootPanel.get("loading").setVisible(true); // Show the loading bar

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
                    } else { // By default load the home page
                        HOME_PAGE.getAndChangeContent(input);
                    }
                }
            }
        });
        logger.log(Level.INFO, "onModuleLoad: added history support");

        // Load the current page, by default the home page
        History.fireCurrentHistoryState();

//        LatLng myLatLng = LatLng.create(49.250, -123.100);
//	    MapOptions myOptions = MapOptions.create();
//	    myOptions.setZoom(12.0);
//	    myOptions.setCenter(myLatLng);
//	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
//	    GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

    }
}
