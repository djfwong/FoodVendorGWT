package com.sneakyxpress.webapp.client.pages.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sneakyxpress.webapp.client.customwidgets.navbars.SideNavbar;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.facebook.*;
import com.sneakyxpress.webapp.client.customwidgets.navbars.tabs.*;
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
                    private final FacebookTools facebook = module.FACEBOOK_TOOLS;
                    private SideNavbar sideNavbar;
                    private User user;

                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(User user) {
                        this.user = user; // Save the user for use in other methods
                        this.sideNavbar = new SideNavbar();

                        // Either show the public or private page
                        if (!facebook.isLoggedIn()) {
                            logger.log(Level.INFO, "User not logged in, showing public page");
                            createPublicPage();
                        } else if (!user.getId().equals(facebook.getUserId())) {
                            logger.log(Level.SEVERE, "Mismatched user IDs, showing public page");
                            createPublicPage();
                        } else { // TODO: We really should have a better security check here!
                            logger.log(Level.INFO, "User logged in, showing private page");
                            createPrivatePage();
                        }

                        module.changeContent(sideNavbar); // Change the page content
                    }


                    /**
                     * Create a private user page (includes settings and private info etc.)
                     */
                    private void createPrivatePage() {
                        // Add a title with the user's name to the page
                        sideNavbar.addTitle(facebook.getUserName(), "Private Profile");

                        // Add private profile tabs
                        sideNavbar.addTab(new PersonalInfoTab(user));
                        sideNavbar.addTab(new FavouritesTab(module, user));
                        sideNavbar.addTab(new FriendsTab(module, user));
                        sideNavbar.addTab(new UserReviewsTab(module, user));

                        // Add specific content for different user types
                        int type = user.getType();
                        if (type == User.OWNER || type == User.ADMIN_AND_OWNER) {
                            sideNavbar.addTab(new OwnerReviewsTab(module, user));
                        }
                        if (type == User.ADMINISTRATOR || type == User.ADMIN_AND_OWNER) {
                            sideNavbar.addTab(new AdministrationTab(module));
                        }

                        // Add the logout tab last
                        sideNavbar.addTab(new LogoutTab(module));
                    }


                    /**
                     * Create a public user page (does not contain settings or sensitive information)
                     */
                    private void createPublicPage() {
                        sideNavbar.addTitle(userId, "Public Profile");
                        sideNavbar.addTab(new PublicTab());
                    }
                });
    }
}
