package com.sneakyxpress.webapp.client.customwidgets.navbars;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.customwidgets.navbars.tabs.AbstractNavbarTab;

/**
 * Created by michael on 11/14/2013.
 */
public class SideNavbar extends Composite {
    private final FlowPanel content = new FlowPanel();
    private final HTMLPanel navTabs = new HTMLPanel("ul", "");
    private final HTMLPanel tabContent = new HTMLPanel("");

    private int tabCounter = 1;


    public SideNavbar() {
        initWidget(content);

        content.addStyleName("tabbable tabs-left");
        navTabs.addStyleName("nav nav-tabs");
        tabContent.addStyleName("tab-content");

        content.add(navTabs);
        content.add(tabContent);
    }


    public void addTab(AbstractNavbarTab tab) {
        // Create the tab entry
        HTMLPanel li = new HTMLPanel("li", "<a href=\"#tab" + tabCounter
                + "\" data-toggle=\"tab\">" + tab.getTitle() + "</a>");
        navTabs.add(li);

        // Create the tab contents
        FlowPanel tabPane = tab.getContent();
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


    public void addTitle(String title, String subTitle) {
        HTMLPanel titleWidget = new HTMLPanel("<h2>" + title
                + "<span class=\"pull-right\"><small class=\"muted\">"
                + subTitle + "</small></span></h2>");
        content.insert(titleWidget, 0);
    }
}
