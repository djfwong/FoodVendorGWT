package com.sneakyxpress.webapp.client.customwidgets.simpletable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Created by michael on 11/13/2013.
 */
public class Row {
    protected Cell[] cells;
    protected HTMLPanel widget;
    protected SimpleTable table;


    public Row(String[] cellLabels, SimpleTable table) {
        this.table = table;
        createCells(cellLabels);

        // Create the row's widget
        widget = new HTMLPanel("tr", "");
        for (Cell c : cells) {
            widget.add(c.getWidget());
        }
    }

    public void addWidgetCell(Widget cell) {
        HTMLPanel td = new HTMLPanel("td", "");
        td.getElement().setAttribute("style", "text-align: center;");
        td.add(cell);
        widget.add(td);
    }


    public HTMLPanel getWidget() {
        return widget;
    }


    public Cell getCell(int column) {
        return cells[column];
    }


    protected void createCells(String[] cellLabels) {
        // Store the row data
        int length = cellLabels.length;
        cells = new Cell[length];

        for (int i = 0; i < length; i++) {
            cells[i] = new Cell(cellLabels[i], i, table);
        }
    }


    public void addClickHandler(ClickHandler handler) {
        widget.sinkEvents(Event.ONCLICK);
        widget.addHandler(handler, ClickEvent.getType());
    }
}
