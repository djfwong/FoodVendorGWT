package com.sneakyxpress.webapp.client.customwidgets.simpletable;


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
}
