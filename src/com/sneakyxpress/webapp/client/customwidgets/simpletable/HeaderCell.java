package com.sneakyxpress.webapp.client.customwidgets.simpletable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;


/**
 * Created by michael on 11/13/2013.
 * Special cells for use in the header.
 */
public class HeaderCell extends Cell {
    private static final InlineLabel DOWN_ARROW = new InlineLabel(" \u25BC");
    private static final InlineLabel UP_ARROW = new InlineLabel(" \u25B2");
    private boolean sortReversed = false;


    public HeaderCell(String label, int column, SimpleTable table) {
        super(label, column, table);
    }


    @Override
    protected void createWidget() {
        // Create the widget
        widget = new HTMLPanel("th", "");
        InlineLabel labelWidget = new InlineLabel(label);
        widget.add(labelWidget);

        // Add a click handler for sorting support
        widget.sinkEvents(Event.ONCLICK);
        widget.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                table.sortRows(column, sortReversed);
            }
        }, ClickEvent.getType());
    }


    public void setSortFlag(boolean sortReversed) {
        this.sortReversed = sortReversed;
    }


    public void setSortSymbol(boolean reverse) {
        InlineLabel arrow = reverse ? UP_ARROW : DOWN_ARROW;
        widget.add(arrow);
    }


    public void removeSortSymbol() {
        widget.remove(UP_ARROW);
        widget.remove(DOWN_ARROW);
    }
}
