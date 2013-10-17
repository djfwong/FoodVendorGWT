package com.sneakyxpress.webapp.client;

import com.google.gwt.user.client.ui.HTML;

/**
 * Used to create the navigation bar and set actions to change the page contents.
 */
public interface Content {
    public static final String GENERIC_ERROR_MESSAGE = "<div class=\"container-fluid\" style=\"padding-top: 20px;\">"
            + "<p class=\"text-error lead\" style=\"text-align: center;\">"
            + "<i class=\"icon-exclamation-sign\"></i> Loading failed! Please try again.</p></div>";

    public String getPageName();
    public String getPageStub();
    public HTML getHTML(String input);
}
