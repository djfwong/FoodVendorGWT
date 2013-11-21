package com.sneakyxpress.webapp.client.customwidgets.simpletable;


import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by michael on 11/13/2013.
 */
public class HeaderRow extends Row {

    public HeaderRow(String[] cellLabels, SimpleTable table) {
        super(cellLabels, table);
    }


    @Override
    protected void createCells(String[] cellLabels) {
        // Store the row data
        int length = cellLabels.length;
        cells = new HeaderCell[length];

        for (int i = 0; i < length; i++) {
            cells[i] = new HeaderCell(cellLabels[i], i, table);
        }
    }

    @Override
    public void addWidgetCell(Widget cell) {
        HTMLPanel th = new HTMLPanel("th", "");
        th.getElement().setAttribute("style", "text-align: center;");
        th.add(cell);
        widget.add(th);
    }
}
