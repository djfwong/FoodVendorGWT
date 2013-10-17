package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sneaky_Xpress implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final HTML SERVER_ERROR = new HTML("<p>An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.</p>");

	/**
	 * Create a remote service proxies to talk to the server-side services.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

    private static final Content[] PAGES = new Content[] {
        new BrowseVendorsContent()
    };

	/**
	 * This is the entry point method.
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
                RootPanel content = RootPanel.get("content");
                content.clear();
                content.add(page.getHTML(""));
            }
        }

        // Create the navigation bar
        RootPanel navbarList = RootPanel.get("navigation-bar");
        for (Content page : PAGES) {
            Anchor pageLink = new Anchor(page.getPageName());

            pageLink.addClickHandler(new NavbarClickHandler(page));

            HTMLPanel listElement = new HTMLPanel("li", "");
            listElement.add(pageLink);
            navbarList.add(listElement);
        }
    }
}
