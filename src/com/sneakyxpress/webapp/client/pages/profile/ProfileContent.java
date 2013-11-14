package com.sneakyxpress.webapp.client.pages.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.facebook.*;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.shared.User;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
                    private int tabCounter = 1;
                    private final HTMLPanel content = new HTMLPanel("");
                    private final HTMLPanel navTabs = new HTMLPanel("ul", "");
                    private final HTMLPanel tabContent = new HTMLPanel("");
                    private final FacebookTools facebook = module.FACEBOOK_TOOLS;
                    private User user;

                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(User user) {
                        this.user = user; // Save the user for use in other methods

                        // Add bootstrap styles to the navigation divs
                        content.addStyleName("tabbable tabs-left");
                        navTabs.addStyleName("nav nav-tabs");
                        tabContent.addStyleName("tab-content");

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

                        // Combine everything
                        content.add(navTabs);
                        content.add(tabContent);

                        module.changeContent(content); // Change the page content
                    }


                    /**
                     * Create a private user page (includes settings and private info etc.)
                     */
                    private void createPrivatePage() {
                        // Add a title with the user's name to the page
                        content.add(new HTML("<div class=\"page-header\"><h2>" + facebook.getUserName()
                                + "<span class=\"pull-right\"><small class=\"muted\"> "
                                + "Private Profile</small></span></h2></div>"));

                        FlowPanel personalInfo = new FlowPanel();
                        personalInfo.add(getInfoWidget("User ID", user.getId()));
                        personalInfo.add(getInfoWidget("User Email", user.getEmail()));
                        personalInfo.add(getInfoWidget("User Type", user.getTypeName()));
                        personalInfo.addStyleName("well");
                        createNewTab("Profile Information", personalInfo);

                        // Add friends (currently not processed by the server)
                        FlowPanel friends = new FlowPanel();
                        SimpleTable friendsTable = new SimpleTable("table-bordered", "Friend Name");
                        for (String f : facebook.getUserFriends().values()) {
                            friendsTable.addRow(f);
                        }
                        friendsTable.sortRows(0, false);
                        friends.add(friendsTable);
                        createNewTab("Friends", friends);

                        // Add specific content for different user types
                        if (user.getType() == User.ADMINISTRATOR) {
                            addAdministratorContent();
                        } else if (user.getType() == User.OWNER) {
                            addOwnerContent();
                        }

                        // Add the logout button last
                        FlowPanel logout = new FlowPanel();
                        logout.add(getButtonWidget(facebook.getLogoutButton()));
                        createNewTab("Logout", logout);
                    }


                    /**
                     * Add administrator content and options to the page
                     */
                    private void addAdministratorContent() {
                        // Add the update data button
                        FlowPanel admin = new FlowPanel();
                        admin.add(getButtonWidget(module.getUpdateDataButton()));
                        createNewTab("Administration", admin);
                    }


                    /**
                     * Add food vendor owner content and options to the page
                     */
                    private void addOwnerContent() {
                        final FlowPanel reviews = new FlowPanel();
                        reviews.addStyleName("well");
                        createNewTab("My Truck's Feedback", reviews);

                        VendorFeedbackServiceAsync vendorFeedbackService = GWT
                                .create(VendorFeedbackService.class);
                        vendorFeedbackService.getVendorFeedback(user.getId(),
                                new AsyncCallback<List<VendorFeedback>>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                module.addMessage(true, "Loading reviews failed! Reason: " +
                                        caught.getMessage());
                            }

                            @Override
                            public void onSuccess(List<VendorFeedback> result) {
                                if (result == null) {
                                    HTMLPanel response = new HTMLPanel("p", "No reviews could be found :(");
                                    response.addStyleName("lead pagination-centered");
                                    reviews.add(response);
                                } else {
                                    Collections.sort(result, new FeedbackComparator());
                                    for (VendorFeedback f : result) {
                                        reviews.add(getReviewWidget(f));
                                    }
                                }
                            }

                            private HTMLPanel getReviewWidget(VendorFeedback f) {
                                String stars = " ";
                                for (int i = 0; i < f.getRating(); i++) {
                                    stars = "<i class=\"icon-star\"></i>" + stars;
                                }

                                HTMLPanel quote = new HTMLPanel("blockquote", "<p>" + stars
                                        + f.getReview() + "</p>");
                                quote.add(new HTMLPanel("small", "User " + userId + " reviewing truck "
                                        + f.getvendorId()));

                                return quote;
                            }

                            class FeedbackComparator implements Comparator<VendorFeedback> {

                                @Override
                                public int compare(VendorFeedback f1, VendorFeedback f2) {
                                    return (int) (f1.getCreationTime() - f2.getCreationTime());
                                }
                            }
                        });
                    }


                    /**
                     * Create a public user page (does not contain settings or sensitive information)
                     */
                    private void createPublicPage() {
                        content.add(new HTML("<div class=\"page-header\"><h2>User ID: "
                                + userId + "<span class=\"pull-right\"><small class=\"muted\"> "
                                + "Public Profile</small></span></h2></div>"));

                        FlowPanel bogus = new FlowPanel();
                        bogus.addStyleName("well");
                        bogus.add(getInfoWidget("Bogus Info", "Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit."));
                        bogus.add(getInfoWidget("More Bogus Info", "Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit."));
                        createNewTab("Bogus Stub", bogus);
                    }


                    /**
                     * Adds a tab to the profile page with the given contents
                     *
                     * @param title     The title of the tab
                     * @param tabPane   The contents to display within the tab
                     */
                    private void createNewTab(String title, FlowPanel tabPane) {
                        // Create the tab entry
                        HTMLPanel li = new HTMLPanel("li", "<a href=\"#tab" + tabCounter
                                + "\" data-toggle=\"tab\">" + title + "</a>");
                        navTabs.add(li);

                        // Create the tab contents
                        tabPane.addStyleName("tab-pane");
                        tabPane.getElement().setId("tab" + tabCounter);
                        tabContent.add(tabPane); // Add the contents to the tab contents div

                        // Make the first tab active
                        if (tabCounter == 1) {
                            li.addStyleName("active");
                            tabPane.addStyleName("active");
                        }

                        tabCounter++;
                    }


                    private HTMLPanel getInfoWidget(String title, String info) {
                        if (info.isEmpty()) {
                            info = "<em class=\"muted\">Information currently not available</em>";
                        }

                        HTMLPanel infoWidget = new HTMLPanel("dl", "<dt>" + title + "</dt>"
                                + "<dd>" + info + "</dd>");
                        infoWidget.addStyleName("dl-horizontal");
                        return infoWidget;
                    }


                    private HTMLPanel getButtonWidget(Button button) {
                        HTMLPanel div = new HTMLPanel("<br>");
                        div.addStyleName("pagination-centered");
                        div.add(button);
                        return div;
                    }
                });
    }
}
