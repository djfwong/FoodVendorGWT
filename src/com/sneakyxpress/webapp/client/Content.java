package com.sneakyxpress.webapp.client;

import com.google.gwt.user.client.ui.HTML;

/**
 * Used to create the navigation bar and set actions to change the page contents.
 */
public interface Content {
    public String getPageName();
    public HTML getHTML(String input);
}
