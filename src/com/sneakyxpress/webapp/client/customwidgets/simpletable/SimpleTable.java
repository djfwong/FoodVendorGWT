package com.sneakyxpress.webapp.client.customwidgets.simpletable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple table that uses Bootstrap styling
 * Created by michael on 11/12/2013.
 */
public class SimpleTable extends Composite {
    private final HTMLPanel tbody = new HTMLPanel("tbody", "");
    private final List<Row> rows = new LinkedList<Row>();
    private HeaderRow headerRow;
    private int previousColumn = 0;


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

        for (int i = 0; i < cells.length; i++) {
            HTMLPanel cell = row.getCell(i).getWidget();
            cell.sinkEvents(Event.ONCLICK);
            cell.addHandler(handler, ClickEvent.getType());
        }

        rows.add(row);
    }

    /**
     * Adds a column with widget cells to the table
     */
    public void addWidgetColumn(String title, List<? extends Widget> widgets) {
        headerRow.addWidgetCell(new InlineLabel(title));
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).addWidgetCell(widgets.get(i));
        }
    }


    /**
     * Sorts the rows then clears the table widget and re-adds the rows in the
     * correct order.
     *
     * @param column    The column to use to compare the rows
     * @param reverse   Should it be sorted in reverse?
     */
    public void sortRows(int column, boolean reverse) {
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
}
