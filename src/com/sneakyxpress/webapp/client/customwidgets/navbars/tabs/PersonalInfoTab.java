package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.user.client.ui.FlowPanel;
import com.sneakyxpress.webapp.shared.User;

/**
 * Created by michael on 11/14/2013.
 */
public class PersonalInfoTab extends AbstractNavbarTab {
    private final User user;

    public PersonalInfoTab(User user) {
        this.user = user;
    }

    @Override
    public String getTitle() {
        return "Profile Information";
    }

    @Override
    public FlowPanel getContent() {
        FlowPanel personalInfo = new FlowPanel();

        personalInfo.add(getInfoWidget("User ID", user.getId()));
        personalInfo.add(getInfoWidget("User Email", user.getEmail()));
        personalInfo.add(getInfoWidget("User Type", user.getTypeName()));

        personalInfo.addStyleName("well");

        return personalInfo;
    }
}
