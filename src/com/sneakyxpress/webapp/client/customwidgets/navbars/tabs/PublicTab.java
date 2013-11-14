package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created by michael on 11/14/2013.
 */
public class PublicTab extends AbstractNavbarTab {

    @Override
    public String getTitle() {
        return "Bogus Tab";
    }

    @Override
    public FlowPanel getContent() {
        FlowPanel bogus = new FlowPanel();
        bogus.addStyleName("well");
        bogus.add(getInfoWidget("Bogus Info", "Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit."));
        bogus.add(getInfoWidget("More Bogus Info", "Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit."));
        return bogus;
    }
}
