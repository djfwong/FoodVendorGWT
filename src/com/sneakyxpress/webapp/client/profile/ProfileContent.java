package com.sneakyxpress.webapp.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
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
                        module.changeContent(new HTML("Test is successful!"));
                    }
                });
    }
}
