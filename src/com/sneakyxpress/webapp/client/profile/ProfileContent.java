package com.sneakyxpress.webapp.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.shared.User;

/**
 * Handles changing the page content to a user profile page.
 */
public class ProfileContent extends Content {
    private String pageName = "Profile Page";
    private String pageStub = "profile";

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
                        module.addMessage(GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(User result) {
                        FacebookTools facebook = module.getFacebookTools();

                        HTMLPanel content = new HTMLPanel(""); // The base panel to hold all content

                        // Contains all the user's information
                        HTMLPanel row = new HTMLPanel("");
                        row.addStyleName("row-fluid");

                        // The first column of information
                        String name = facebook.getUserName();
                        if (name.isEmpty()) {
                            name = "<em class=\"muted\">No Name Available</em>";
                        }

                        HTMLPanel col1 = new HTMLPanel("<div class=\"page-header\"><h2>" + name + "</h2></div>");
                        col1.addStyleName("span6");

                        col1.add(getInfoWidget("Bogus Info 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
                        col1.add(getInfoWidget("Bogus Info 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
                        col1.add(getInfoWidget("Bogus Info 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));

                        // The second column of information
                        HTMLPanel col2 = new HTMLPanel("");
                        col2.addStyleName("span6");

                        col2.add(getInfoWidget("Bogus Info 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
                        col2.add(getInfoWidget("Bogus Info 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
                        col2.add(getInfoWidget("Bogus Info 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));

                        // Add all the content together
                        row.add(col1);
                        row.add(col2);
                        content.add(row);

                        module.changeContent(content); // Change the page content
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
