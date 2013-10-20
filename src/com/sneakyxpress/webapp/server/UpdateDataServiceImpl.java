package com.sneakyxpress.webapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Updates the Food Vendor data from datavancouver.com
 */
public class UpdateDataServiceImpl extends RemoteServiceServlet {
    private static final PersistenceManagerFactory PMF =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");
    private static Logger logger = Logger.getLogger("");

    public void parseData() {
        PersistenceManager pm = getPersistenceManager();

        try {
            FileInputStream file = new FileInputStream("data/new_food_vendor_locations.xls");

            // Get the first sheet from the excel file
            HSSFSheet sheet = (new HSSFWorkbook(file)).getSheetAt(0);

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
            Query findOld = pm.newQuery("SELECT FROM " + FoodVendor.class.getCanonicalName());
            findOld.deletePersistentAll();
            // TODO: Delete all the old FoodVendor information (the above code is not working)
            pm.makePersistentAll(newVendors);

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "UpdateDataServiceImpl: FileNotFoundException " + e.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "UpdateDataServiceImpl: IOException " + e.toString());
        } finally {
            pm.close();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        parseData();
        // TODO: Fetch the xls file from DataVancouver instead of using a local version
        logger.log(Level.INFO, "Retrieved new data from DataVancouver");
        // TODO: Delete the xls file
    }

    private PersistenceManager getPersistenceManager() {
        return PMF.getPersistenceManager();
    }
}
