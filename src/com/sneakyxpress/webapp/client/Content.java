package com.sneakyxpress.webapp.client;

import java.util.logging.Logger;

/**
 * Used to create the navigation bar and set actions to change the page contents.
 */
public abstract class Content {
    public static final Logger logger = Logger.getLogger("");
    public static Sneaky_Xpress module;

    public static final String GENERIC_ERROR_MESSAGE = "Loading failed! Please try again.";

    public Content(Sneaky_Xpress module) {
        this.module = module;
    }

    public abstract String getPageName();
    public abstract String getPageStub();
    public abstract void getAndChangeContent(String input);
}
