package com.sneakyxpress.webapp.client.simpletable;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;


/**
 * Created by michael on 11/13/2013.
 * A cell for use in a row.
 */
public class Cell {
    protected String label;
    protected int column;
    protected HTMLPanel widget;
    protected SimpleTable table;


    public Cell(String label, int column, SimpleTable table) {
        // Store the cell's data
        this.label = label;
        this.column = column;
        this.table = table;

        // Create the cell's widget
        createWidget();
    }


    public HTMLPanel getWidget() {
        return widget;
    }


    public String getLabel() {
        return label;
    }


    protected void createWidget() {
        widget = new HTMLPanel("td", "");
        widget.add(new Label(label));
    }
}
