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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
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

					public void onSuccess(List<FoodVendor> result) {
						HTMLPanel content = new HTMLPanel(""); // The new content to return

						// The sub-navigation bar
						HTMLPanel list = new HTMLPanel("ul", "");
						list.addStyleName("nav nav-tabs");

						// The tabs in the sub-navigation bar
						HTMLPanel listView = new HTMLPanel("li", "");
						HTMLPanel mapView = new HTMLPanel("li", "");

						// Links for the tabs
						Anchor listViewLink = new Anchor("List View");
						listViewLink.addClickHandler(new PageClickHandler(
								BrowseVendorsContent.this, "list"));
						listView.add(listViewLink);

						Anchor mapViewLink = new Anchor("Map View");
						mapViewLink.addClickHandler(new PageClickHandler(
								BrowseVendorsContent.this, "map"));
						mapView.add(mapViewLink);

						// Create the sub-navigation bar
						list.add(listView);
						list.add(mapView);
						content.add(list);

						// Load the appropriate view
						if (input.equals("map")) {
							mapView.addStyleName("active");
							listView.removeStyleName("active");
							
							// Add the map etc.
                            HTMLPanel mapDiv = new HTMLPanel("");
                            mapDiv.addStyleName("map_canvas");
                            mapDiv.setSize("100%", "500px");

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


