package com.sneakyxpress.webapp.client.pages.browsevendors;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.sneakyxpress.webapp.client.*;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The contents of the Browse Vendors page
 */
public class BrowseVendorsContent extends Content {
	private static final String pageName = "Browse Vendors";
	private static final String pageStub = "browse";
	
	private GoogleMap map;

	private final BrowseVendorsServiceAsync browseVendorsService = GWT
			.create(BrowseVendorsService.class);

	public BrowseVendorsContent(Sneaky_Xpress module) {
		super(module);
	}

	@Override
	public String getPageName() {
		return pageName;
	}

	@Override
	public String getPageStub() {
		return pageStub;
	}

	@Override
	public void getAndChangeContent(final String input) {
		browseVendorsService.browseVendorsServer(input,
				new AsyncCallback<List<FoodVendor>>() {
					public void onFailure(Throwable caught) {
						module.addMessage(true, GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
					}

					public void onSuccess(final List<FoodVendor> result) {
						final HTMLPanel content = new HTMLPanel(""); // The new content to return

						// The sub-navigation bar
						HTMLPanel list = new HTMLPanel("ul", "");
						list.addStyleName("nav nav-tabs");

						// The tabs in the sub-navigation bar
						HTMLPanel listView = new HTMLPanel("li", "");
						HTMLPanel mapView = new HTMLPanel("li", "");
                        HTMLPanel localView = new HTMLPanel("li", "");

						// Links for the tabs
						Anchor listViewLink = new Anchor("List View");
						listViewLink.addClickHandler(new PageClickHandler(
								BrowseVendorsContent.this, "list"));
						listView.add(listViewLink);

						Anchor mapViewLink = new Anchor("Map View");
						mapViewLink.addClickHandler(new PageClickHandler(
								BrowseVendorsContent.this, "map"));
						mapView.add(mapViewLink);

                        Anchor localViewLink = new Anchor("Vendors Near Me");
                        localViewLink.addClickHandler(new PageClickHandler(
                                BrowseVendorsContent.this, "local"));
                        localView.add(localViewLink);

						// Create the sub-navigation bar
						list.add(listView);
						list.add(mapView);
                        list.add(localView);

						content.add(list);

						// Load the appropriate view
						if (input.equals("map")) {
							// Map view
							mapView.addStyleName("active");
							listView.removeStyleName("active");
                            localView.removeStyleName("active");

                            // Add instructions
                            content.add(new HTML("<p class=\"lead pagination-centered\">"
                                    + "Hover over a pin for a description of the vendor. "
                                    + "Click on the pin to view the vendor's profile page.</p>"));

                            // Change the page content
                            content.add(new HTML("<div id=\"map_canvas\"></div>"));
                            module.changeContent(content);

							// Add map
	                        LatLng myLatLng = LatLng.create(49.250, -123.100);
	                	    MapOptions myOptions = MapOptions.create();
	                	    myOptions.setZoom(12.0);
	                	    myOptions.setCenter(myLatLng);
	                	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	                	    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

	                	    // Get user's location
	                	    if (Geolocation.isSupported()) {
	                	    	Geolocation geo = Geolocation.getIfSupported();
	                	    	geo.getCurrentPosition(new Callback<Position, PositionError>() {

                                    @Override
	                	    		public void onSuccess(Position position) {
	                	    			Coordinates c = position.getCoordinates();
	                	    			
	                	    			MarkerOptions newMarkerOpts = MarkerOptions.create();
	                	    			newMarkerOpts.setPosition(LatLng.create(c.getLatitude(), c.getLongitude()));

	                	    			// Custom icon from my Dropbox
	                	    			newMarkerOpts.setIcon(MarkerImage.create("/sneaky_xpress/img/user.png"));
	                            	    newMarkerOpts.setMap(map);
	                            	    newMarkerOpts.setTitle("Your Location");
	                            	    Marker.create(newMarkerOpts);
	                	    		}

	    							@Override
	    							public void onFailure(PositionError reason) {
	    								logger.log(Level.SEVERE, "Error adding user marker. Reason: " + reason.getMessage());
	    							}
	                	    	});
	                	    }
	                	    
	                	    // Plot POIs
                            for (final FoodVendor tmp : result) {
                                MarkerOptions newMarkerOpts = MarkerOptions.create();
                                newMarkerOpts.setPosition(LatLng.create(tmp.getLatitude(), tmp.getLongitude()));
                                newMarkerOpts.setMap(map);
                                if (tmp.getName().equals("")) {
                                    newMarkerOpts.setTitle(tmp.getDescription());
                                } else {
                                    newMarkerOpts.setTitle(tmp.getName());
                                }
                                final Marker marker = Marker.create(newMarkerOpts);

                                marker.addClickListener(new ClickHandler() {
                                    public void handle(MouseEvent event) {
                                        History.newItem(module.VENDOR_PAGE.getPageStub() + "?" + tmp.getVendorId());
                                    }
                                });
                            }
						} else if (input.equals("local")) {
							// Vendors Near Me view
                            localView.addStyleName("active");
                            listView.removeStyleName("active");
                            mapView.removeStyleName("active");

                            // Add instructions
                            content.add(new HTML("<p class=\"lead pagination-centered\">"
                                    + "Hover over a pin for a description of the vendor. " +
                                    "Click on the pin to view the vendor's profile page.</p>"));

                            // Change the page content
                            content.add(new HTML("<div id=\"map_canvas_2\"><p class=\"lead\" id=\"geoMessage\" "
                                    + "style=\"text-align: center;\">Please wait. Getting your location...</p></div>"));
                            module.changeContent(content);

                            // Get user's location
                            if (Geolocation.isSupported()) {
                                Geolocation geo = Geolocation.getIfSupported();
                                geo.getCurrentPosition(new Callback<Position, PositionError>() {

                                    @Override
                                    public void onSuccess(Position position) {
                                        Coordinates c = position.getCoordinates();

                                        // Add map
                                        LatLng userLatLng = LatLng.create(c.getLatitude(), c.getLongitude());
                                        MapOptions myOptions = MapOptions.create();
                                        myOptions.setZoom(16.0);
                                        myOptions.setCenter(userLatLng);
                                        myOptions.setMapTypeId(MapTypeId.ROADMAP);
                                        map = GoogleMap.create(Document.get().getElementById("map_canvas_2"), myOptions);

                                        // Add the user marker
                                        MarkerOptions newMarkerOpts = MarkerOptions.create();
                                        newMarkerOpts.setPosition(userLatLng);
                                        newMarkerOpts.setIcon(MarkerImage.create("https://dl.dropboxusercontent.com/u/15430100/user.png"));
                                        newMarkerOpts.setMap(map);
                                        newMarkerOpts.setTitle("Your Location");
                                        Marker.create(newMarkerOpts);

                                        // Plot POIs
                                        for (final FoodVendor tmp : result) {
                                            MarkerOptions tempMarkerOpts = MarkerOptions.create();
                                            tempMarkerOpts.setPosition(LatLng.create(tmp.getLatitude(), tmp.getLongitude()));
                                            tempMarkerOpts.setMap(map);

                                            if (tmp.getName().equals("")) {
                                                tempMarkerOpts.setTitle(tmp.getDescription());
                                            } else {
                                                tempMarkerOpts.setTitle(tmp.getName());
                                            }

                                            final Marker marker = Marker.create(tempMarkerOpts);

                                            marker.addClickListener(new ClickHandler() {
                                                public void handle(MouseEvent event) {
                                                    History.newItem(module.VENDOR_PAGE.getPageStub() + "?" + tmp.getVendorId());
                                                }
                                            });
                                        }
                                    }

                                    // If getting Geolocation fails
                                    @Override
                                    public void onFailure(PositionError reason) {
                                    	content.getElementById("geoMessage").setInnerHTML("We couldn't find your location!");
                                    }
                                });
                            }
                        } else {
							// By default load the list view
							listView.addStyleName("active");
							mapView.removeStyleName("active");
                            localView.removeStyleName("active");

                            // Add instructions
                            content.add(new HTML("<p class=\"lead pagination-centered\">"
                                    + "Click on a row in the table to view the vendor's profile page.</p>"));

							// Add data table under List view
                            SimpleTable table = new SimpleTable("table-hover table-bordered table-striped",
                                    "Key", "Name", "Description", "Location");
                            for (FoodVendor v : result) {
                                table.addRow(new PageClickHandler(module.VENDOR_PAGE, v.getVendorId()),
                                        v.getVendorId(), v.getName(), v.getDescription(), v.getLocation());
                            }
                            table.sortRows(0, false); // Sort the rows

							content.add(table);

                            module.changeContent(content); // Change the page content
                        }
					}
				});
    }
}
