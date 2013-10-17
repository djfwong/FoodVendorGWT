package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sneaky_Xpress implements EntryPoint {
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
                changeContent(page);
                History.newItem(page.getPageStub());
            }
        }

        // Create the brand/title in the navigation bar
        Anchor brandLink = new Anchor(HOME_PAGE.getPageName());
        brandLink.addClickHandler(new NavbarClickHandler(HOME_PAGE));
        brandLink.addStyleName("brand");
        RootPanel.get("brand").add(brandLink);

        // Load the homepage
        changeContent(HOME_PAGE);

        // Create the navigation bar
        RootPanel navbarList = RootPanel.get("navigation-bar");
        for (Content page : PAGES) {
            Anchor pageLink = new Anchor(page.getPageName());

            pageLink.addClickHandler(new NavbarClickHandler(page));

            HTMLPanel listElement = new HTMLPanel("li", "");
            listElement.add(pageLink);
            navbarList.add(listElement);
        }

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
    }
}
