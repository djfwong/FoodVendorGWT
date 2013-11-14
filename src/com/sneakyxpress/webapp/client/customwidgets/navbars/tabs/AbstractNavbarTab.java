package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;


/**
 * Created by michael on 11/14/2013.
 */
public abstract class AbstractNavbarTab {
    public abstract String getTitle();
    public abstract FlowPanel getContent();


    protected HTMLPanel getInfoWidget(String title, String info) {
        if (info.isEmpty()) {
            info = "<em class=\"muted\">Information currently not available</em>";
        }

        HTMLPanel infoWidget = new HTMLPanel("dl", "<dt>" + title + "</dt>"
                + "<dd>" + info + "</dd>");
        infoWidget.addStyleName("dl-horizontal");
        return infoWidget;
    }


    protected HTMLPanel getButtonWidget(Button button) {
        HTMLPanel div = new HTMLPanel("<br>");
        div.addStyleName("pagination-centered");
        div.add(button);
        return div;
    }
}
