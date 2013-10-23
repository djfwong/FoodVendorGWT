package com.sneakyxpress.webapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.shared.FoodVendor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Updates the Food Vendor data from DataVancouver
 */
public class UpdateDataServiceImpl extends RemoteServiceServlet {
    private static Logger logger = Logger.getLogger("");
    private static final String DATA_LOCATION
            = "http://www.ugrad.cs.ubc.ca/~k5r8/data/uploads/new_food_vendor_locations.xls";

    /**
     * Parse the new Food Vendor data
     * @param stream    The stream to parse
     * @return          The number of Food Vendors parsed (for debugging & logging purposes)
     */
    public int[] parseData(InputStream stream) throws IOException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        // Added is changes[0], removed is changes[1]
        int[] changes = {0, 0};

        // Get the first sheet from the excel file
        HSSFSheet sheet = (new HSSFWorkbook(stream)).getSheetAt(0);

        // Iterate through each row of the first sheet
        LinkedList<FoodVendor> newVendors = new LinkedList<FoodVendor>();
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip the first line (headers)
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each column
            // Make sure it is not an empty row
            Cell vendorId = row.getCell(0);
            if (vendorId.getCellType() == Cell.CELL_TYPE_STRING) {
                FoodVendor vendor = new FoodVendor();

                // Set all of our data fields
                vendor.setVendorId(vendorId.getStringCellValue());

                Cell name = row.getCell(3);
                if (name != null && name.getCellType() == Cell.CELL_TYPE_STRING) {
                    vendor.setName(name.getStringCellValue());
                }

                Cell location = row.getCell(4);
                if (location != null && location.getCellType() == Cell.CELL_TYPE_STRING) {
                    vendor.setLocation(location.getStringCellValue());
                }

                Cell description = row.getCell(5);
                if (description != null && description.getCellType() == Cell.CELL_TYPE_STRING) {
                    vendor.setDescription(description.getStringCellValue());
                }

                Cell latitude = row.getCell(6);
                if (latitude != null && latitude.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    vendor.setLatitude(latitude.getNumericCellValue());
                }

                Cell longitude = row.getCell(7);
                if (longitude != null && longitude.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    vendor.setLongitude(longitude.getNumericCellValue());
                }

                // Collect the new Food Vendors
                newVendors.add(vendor);
            }
        }

        // If we've made it this far without exceptions, update the Food Vendor info
        // Remove the old food vendors
        Query q = pm.newQuery();
        q.setClass(FoodVendor.class);
        changes[1] = (int) q.deletePersistentAll();

        // Persist the new vendors
        pm.makePersistentAll(newVendors);
        changes[0] = newVendors.size();

        // Clean-up
        pm.close();
        stream.close();
        return changes;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.log(Level.INFO, "Attempting to retrieve new data from DataVancouver");

        // Retrieve the remote data
        URL url = new URL(DATA_LOCATION);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            int changes[] = parseData(connection.getInputStream()); // Parse the data
            logger.log(Level.INFO, "Retrieved & parsed data successfully: " + String.valueOf(changes[1])
                    + " vendors removed, " + String.valueOf(changes[0]) + " vendors added");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Retrieving or parsing of data failed: " + e.toString());
        }
    }
}
