package com.sneakyxpress.webapp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A simple table that uses Bootstrap styling
 * Created by michael on 11/12/2013.
 */
public class SimpleTable extends Composite {
    private final HTMLPanel tbody = new HTMLPanel("tbody", "");
    private final List<Row> rows = new LinkedList<Row>();
    private HeaderRow headerRow;
    private int previousColumn = 0;
    private static final Logger logger = Logger.getLogger("");


    /**
     * Creates a new simple table using the specified Bootstrap style
     * and columns.
     *
     * @param styles    Either one or more Bootstrap table styles, all in one String
     *                  separated by a space (ex. "table-striped table-hover").
     * @param columns   The columns of the table
     */
    public SimpleTable(String styles, String... columns) {
        // The entire table element
        HTMLPanel table = new HTMLPanel("table", "");
        initWidget(table);
        table.addStyleName("table");
        table.addStyleName(styles);

        // Create the header
        HTMLPanel thead = new HTMLPanel("thead", "");
        headerRow = new HeaderRow(columns, this);
        thead.add(headerRow.getWidget());

        // Combine everything
        table.add(thead);
        table.add(tbody);
    }


    /**
     * Adds a row to the end of the table using the specified cells. We assume all rows
     * have the same number of columns (which matches the number of headers).
     *
     *
     * @param cells     The cells to use to construct the row.
     */
    public void addRow(String... cells) {
        Row row = new Row(cells, this);
        rows.add(row);
    }


    /**
     * Adds a row to the end of the table using the specified cells. We assume all rows
     * have the same number of columns (which matches the number of headers).
     * Also adds a link to the entire row using the provided ClickHandler.
     *
     * @param handler   The handler to call when the row is clicked.
     * @param cells     The cells to use to construct the row.
     */
    public void addRow(ClickHandler handler, String... cells) {
        Row row = new Row(cells, this);
        row.addClickHandler(handler);
        rows.add(row);
    }


    /**
     * Sort the rows according to the give column, in alphabetical order.
     *
     * @param column    The column to use to compare the rows
     */
    public void sortRows(int column) {
        // Sort the rows
        Collections.sort(rows, new RowComparator(column));

        // Clear & add the rows back to the widget
        tbody.clear();
        for (Row r : rows) {
            tbody.add(r.getWidget());
        }

        // Set the column to sort in reverse next time
        resetSortFlags(column, false);
    }


    /**
     * Sorts the rows then clears the table widget and re-adds the rows in the
     * correct order.
     *
     * @param column    The column to use to compare the rows
     * @param reverse   Should it be sorted in reverse?
     */
    private void sortRows(int column, boolean reverse) {
        // Sort the rows
        Collections.sort(rows, new RowComparator(column));
        if (reverse) {
            Collections.reverse(rows);
        }

        // Clear & add the rows back to the widget
        tbody.clear();
        for (Row r : rows) {
            tbody.add(r.getWidget());
        }

        resetSortFlags(column, reverse);
    }


    private void resetSortFlags(int column, boolean reverse) {
        ((HeaderCell) headerRow.getCell(previousColumn)).setSortFlag(false);
        ((HeaderCell) headerRow.getCell(previousColumn)).removeSortSymbol();
        ((HeaderCell) headerRow.getCell(column)).setSortFlag(!reverse);
        ((HeaderCell) headerRow.getCell(column)).setSortSymbol(reverse);
        previousColumn = column;
    }


    private class Row {
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


    private class HeaderRow extends Row {

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


    /**
     * A cell for use in a row
     */
    private class Cell {
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


    /**
     * Special cells for use in the header
     */
    private class HeaderCell extends Cell {
        private final InlineLabel DOWN_ARROW = new InlineLabel(" \u25BC");
        private final InlineLabel UP_ARROW = new InlineLabel(" \u25B2");
        boolean sortReversed = false;

        public HeaderCell(String label, int column, SimpleTable table) {
            super(label, column, table);
        }

        @Override
        protected void createWidget() {
            widget = new HTMLPanel("th", "");
            InlineLabel labelWidget = new InlineLabel(label);
            widget.add(labelWidget);

            widget.sinkEvents(Event.ONCLICK);
            widget.addHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    logger.log(Level.INFO, "Sorting the table by column " + column
                            + ". Reversed? " + sortReversed);
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


    /**
     * Used to sort rows given a column index
     */
    private class RowComparator implements Comparator<Row> {
        private int column;

        public RowComparator(int column) {
            this.column = column;
        }

        @Override
        public int compare(Row row1, Row row2) {
            if (row1 == row2) {
                return 0;
            }

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
}
