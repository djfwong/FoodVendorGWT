package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.user.client.ui.FlowPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;

/**
 * Created by michael on 11/14/2013.
 */
public class FriendsTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;

    public FriendsTab(Sneaky_Xpress module) {
        this.module = module;
    }

    @Override
    public String getTitle() {
        return "Friends";
    }

    @Override
    public FlowPanel getContent() {
        FlowPanel friends = new FlowPanel();
        SimpleTable friendsTable = new SimpleTable("table-bordered", "Friend Name");
        for (String f : module.FACEBOOK_TOOLS.getUserFriends().values()) {
            friendsTable.addRow(f);
        }
        friendsTable.sortRows(0, false);
        friends.add(friendsTable);
        return friends;
    }
}
