package com.sneakyxpress.webapp.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.facebook.*;
import com.sneakyxpress.webapp.shared.User;

import java.util.logging.Level;

/**
 * Handles changing the page content to a user profile page.
 */
public class ProfileContent extends Content {
    private static final String pageName = "Profile Page";
    private static final String pageStub = "profile";

    private final ProfileServiceAsync profileService = GWT.create(ProfileService.class);

    public ProfileContent(Sneaky_Xpress module) {
        super(module);
    }

    @Override
    public String getPageName() {
        return pageName;
    }

    @Override
    public String getPageStub() {
        return pageStub;
    }

    @Override
    public void getAndChangeContent(final String userId) {
        profileService.getUserInfo(userId,
                new AsyncCallback<User>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(User user) {
                        FacebookTools facebook = module.FACEBOOK_TOOLS;

                        HTMLPanel content = new HTMLPanel(""); // The base panel to hold all content

                        // Create the structure of the page
                        HTMLPanel row = new HTMLPanel("");
                        row.addStyleName("row-fluid");

                        HTMLPanel col1 = new HTMLPanel("");
                        col1.addStyleName("span6");
                        HTMLPanel col2 = new HTMLPanel("");
                        col2.addStyleName("span6");

                        // Either show the public or private page
                        if (!facebook.isLoggedIn()) {
                            logger.log(Level.INFO, "User not logged in, showing public page");
                            createPublicPage(user, col1, col2);
                        } else if (!user.getId().equals(facebook.getUserId())) {
                            logger.log(Level.SEVERE, "Mismatched user IDs, showing public page");
                            createPublicPage(user, col1, col2);
                        } else { // TODO: We really should have a better security check here!
                            logger.log(Level.INFO, "User logged in, showing private page");
                            createPrivatePage(user, col1, col2);
                        }

                        // Put it all together
                        row.add(col1);
                        row.add(col2);
                        content.add(row);

                        module.changeContent(content); // Change the page content
                    }


                    /**
                     * Create a private user page (includes settings and private info etc.)
                     *
                     * @param user      The user who's page we're displaying
                     * @param col1      The first column of the page
                     * @param col2      The second column of the page
                     */
                    private void createPrivatePage(User user, HTMLPanel col1, HTMLPanel col2) {
                        FacebookTools facebook = module.FACEBOOK_TOOLS;

                        col1.add(new HTML("<div class=\"page-header\"><h2>" + facebook.getUserName()
                                + " <small class=\"muted\"> Private Profile</small></h2></div>"));

                        col1.add(getInfoWidget("User ID", user.getId()));
                        col1.add(getInfoWidget("User Email", user.getEmail()));

                        col2.add(getInfoWidget("Bogus Info", "Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit."));
                        col2.add(getInfoWidget("More Bogus Info", "Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit."));

                        HTMLPanel logout = new HTMLPanel("<br>");
                        logout.addStyleName("pagination-centered");
                        logout.add(facebook.getLogoutButton());

                        col2.add(logout);
                    }


                    /**
                     * Create a public user page (does not contain settings or sensitive information)
                     *
                     * @param user      The user who's page we're displaying
                     * @param col1      The first column of the page
                     * @param col2      The second column of the page
                     */
                    private void createPublicPage(User user, HTMLPanel col1, HTMLPanel col2) {
                        FacebookTools facebook = module.FACEBOOK_TOOLS;

                        col1.add(new HTML("<div class=\"page-header\"><h2>User ID: "
                                + userId + " <small class=\"muted\"> Public Profile</small>"
                                + "</h2></div>"));

                        col2.add(getInfoWidget("Bogus Info", "Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit."));
                        col2.add(getInfoWidget("More Bogus Info", "Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit."));
                    }


                    private HTML getInfoWidget(String title, String info) {
                        if (info.isEmpty()) {
                            info = "<em class=\"muted\">Information currently not available</em>";
                        }

                        return new HTML("<h3>" + title + "</h3>" + "<p>" + info + "</p>");
                    }
                });
    }
}
