package com.sneakyxpress.webapp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;

/**
 * Handles actions invoked by clicking links in the navigation bar
 */
class PageClickHandler implements ClickHandler {
    Content page;
    String input;

    public PageClickHandler(Content page, String input) {
        super();
        this.page = page;
        this.input = input;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (input.equals("")) {
            History.newItem(page.getPageStub());
        } else {
            History.newItem(page.getPageStub() + "?" + input);
        }
    }
}
