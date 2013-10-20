package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
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
    private static final Content[] PAGES = new Content[] {
        new BrowseVendorsContent()
    };

    /**
     * Changes the contents of the page to the specified Content
     */
    private void changeContent(Content content) {
        RootPanel body = RootPanel.get("content");
        body.clear();
        body.add(content.getHTML(""));
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

        // Implements history support
        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                String historyToken = event.getValue();

                // Parse the history token
                boolean foundPage = false;
                for (Content page : PAGES) {
                    if (page.getPageStub().equals(historyToken)) {
                        changeContent(page);
                        foundPage = true;
                        break;
                    }
                }

                // By default return to the home page
                if (!foundPage) {
                    changeContent(HOME_PAGE);
                }
            }
        });
        logger.log(Level.INFO, "onModuleLoad: added history support");

        // Load the current page, by default the home page
        History.fireCurrentHistoryState();
    }
}
