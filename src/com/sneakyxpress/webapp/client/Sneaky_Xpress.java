package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

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
    private static final Content HOME_PAGE = new GreetingContent();
    private static final Content SEARCH_PAGE = new SearchContent();
    private static final Content[] PAGES = new Content[] {
        new BrowseVendorsContent()
    };

    /**
     * Changes the contents of the page to the specified Content
     */
    private void changeContent(Content content, String input) {
        RootPanel body = RootPanel.get("content");
        body.clear();
        body.add(content.getContent(input));
        logger.log(Level.INFO, "changeContent: " + content.getPageStub());
    }
    

    /**
     * Adds a message to the top of the page (user can close it)
     */
    private void addMessage(String info) {
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
                        changeContent(page, input);
                        foundPage = true;
                        break;
                    }
                }

                if (!foundPage) {
                    if (SEARCH_PAGE.getPageStub().equals(pageStub)) {
                        changeContent(SEARCH_PAGE, input);
                    } else { // By default load the home page
                        changeContent(HOME_PAGE, input);
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
