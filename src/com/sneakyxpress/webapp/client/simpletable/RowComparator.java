package com.sneakyxpress.webapp.client.simpletable;

import java.util.Comparator;


/**
 * Created by michael on 11/13/2013.
 * Used to sort rows given a column index.
 */
public class RowComparator implements Comparator<Row> {
    private int column;


    public RowComparator(int column) {
        this.column = column;
    }


    @Override
    public int compare(Row row1, Row row2) {
        String label1 = row1.getCell(column).getLabel();
        String label2 = row2.getCell(column).getLabel();

        // We assume the rows and cells are not null
        if (label1.isEmpty()) {
            return 1;
        } else if (label2.isEmpty()) {
            return -1;
        } else {
            return label1.compareTo(label2);
        }
    }
}
