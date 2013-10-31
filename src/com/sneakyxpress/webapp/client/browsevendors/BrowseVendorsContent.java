package com.sneakyxpress.webapp.client.browsevendors;

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
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.FoodVendorDisplayTable;
import com.sneakyxpress.webapp.client.PageClickHandler;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
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
						module.addMessage(GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
					}

					public void onSuccess(final List<FoodVendor> result) {
						HTMLPanel content = new HTMLPanel(""); // The new content to return

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
							mapView.addStyleName("active");
							listView.removeStyleName("active");
                            localView.removeStyleName("active");

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
	                	    			newMarkerOpts.setIcon(MarkerImage.create("https://dl.dropboxusercontent.com/u/15430100/user.png"));
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

	                	    final InfoWindow infowindow = InfoWindow.create();
	                	    
	                	    // Plot POIs
	                	    for(int i = 0; i < result.size(); i++) {
	                	    	final FoodVendor tmp = result.get(i);
	                	    	
	                	    	MarkerOptions newMarkerOpts = MarkerOptions.create();
	                    	    newMarkerOpts.setPosition(LatLng.create(tmp.getLatitude(), tmp.getLongitude()));
	                    	    newMarkerOpts.setMap(map);
	                    	    if (tmp.getName().equals("")){
	                    	    	newMarkerOpts.setTitle(tmp.getDescription());
	                    	    }
	                    	    else {
	                    	    	newMarkerOpts.setTitle(tmp.getName());
	                    	    }
	                    	    final Marker marker = Marker.create(newMarkerOpts);
	                    	    	                    	   	                    	    
	                    	    marker.addClickListener(new ClickHandler() {
	                    	    	public void handle(MouseEvent event) {
	                    	    		History.newItem(module.getVendorPage().getPageStub() + "?" + tmp.getVendorId());
	                    	    	}
	                    	    });
	                	    }
						} else if (input.equals("local")) {
                            localView.addStyleName("active");
                            listView.removeStyleName("active");
                            mapView.removeStyleName("active");

                            // Change the page content
                            content.add(new HTML("<div id=\"map_canvas\"><p class=\"lead\" "
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
                                        map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

                                        // Add the user marker
                                        MarkerOptions newMarkerOpts = MarkerOptions.create();
                                        newMarkerOpts.setPosition(userLatLng);
                                        newMarkerOpts.setIcon(MarkerImage.create("https://dl.dropboxusercontent.com/u/15430100/user.png"));
                                        newMarkerOpts.setMap(map);
                                        newMarkerOpts.setTitle("Your Location");
                                        Marker.create(newMarkerOpts);

                                        final InfoWindow infowindow = InfoWindow.create();

                                        // Plot POIs
                                        for(int i = 0; i < result.size(); i++) {
                                            final FoodVendor tmp = result.get(i);

                                            MarkerOptions tempMarkerOpts = MarkerOptions.create();
                                            tempMarkerOpts.setPosition(LatLng.create(tmp.getLatitude(), tmp.getLongitude()));
                                            tempMarkerOpts.setMap(map);
                                            tempMarkerOpts.setTitle(tmp.getName());
                                            final Marker marker = Marker.create(tempMarkerOpts);

                                            marker.addClickListener(new ClickHandler() {
                                                public void handle(MouseEvent event) {
                                                    infowindow.setContent(tmp.getDescription());
                                                    infowindow.open(map, marker);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(PositionError reason) {
                                        logger.log(Level.SEVERE, "Error getting user's location. Reason: " + reason.getMessage());
                                    }
                                });
                            }
                        } else {
							// By default load the list view
							listView.addStyleName("active");
							mapView.removeStyleName("active");

							// Add data table under List view
							Widget table = new FoodVendorDisplayTable(result, module.getVendorPage()).getWidget();
							content.add(table);

                            module.changeContent(content); // Change the page content
                        }
					}
				});
    }
}
