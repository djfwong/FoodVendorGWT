package com.sneakyxpress.webapp.client.greeting;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;

/**
 * Get the content for our home page.
 */
public class GreetingContent extends Content {
    private static final String pageName = "Vancouver Food Vendor Reviews";
    private static final String pageStub = "home";

    private Marker marker;
    private GoogleMap map;
    
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    public GreetingContent(Sneaky_Xpress module) {
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
    public void getAndChangeContent(String input) {
        greetingService.greetServer(input, new AsyncCallback<String>() {
                public void onFailure(Throwable caught) {
                    module.addMessage(GENERIC_ERROR_MESSAGE + " Reason: " + caught.getMessage());
                }

                public void onSuccess(String result) {
                    module.changeContent(new HTML(result));
                    
                    /* Not permanent, map is to be on Browse Vendors page. Just for testing
                     * Code is SUPER messy. Forgive me. Will pull it all up into its own classes.
                     */
                    LatLng myLatLng = LatLng.create(49.250, -123.100);
            	    
                    MapOptions myOptions = MapOptions.create();
            	    myOptions.setZoom(12.0);
            	    myOptions.setCenter(myLatLng);
            	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
            	    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
            	    
            	    InfoWindowOptions infowindowOpts = InfoWindowOptions.create();
            	    infowindowOpts.setContent("Hello World!");
            	    final InfoWindow infowindow = InfoWindow.create(infowindowOpts);
            	    
            	    MarkerOptions newMarkerOpts = MarkerOptions.create();
            	    newMarkerOpts.setPosition(myLatLng);
            	    newMarkerOpts.setMap(map);
            	    newMarkerOpts.setTitle("Hello World!");
            	    marker = Marker.create(newMarkerOpts);
            	    
            	    marker.addClickListener(new ClickHandler() {
            	    	public void handle(MouseEvent event) {
            	    		infowindow.open(map, marker);
            	    	}
            	    });
            	    
            	    if (Geolocation.isSupported()) {
            	    	Geolocation geo = Geolocation.getIfSupported();
            	    	geo.getCurrentPosition(new Callback<Position, PositionError>() {
            	    		           	    	
            	    		public void onSuccess(Position position) {
            	    			Coordinates c = position.getCoordinates();
            	    			
            	    			MarkerOptions newMarkerOpts = MarkerOptions.create();
            	    			LatLng user = LatLng.create(c.getLatitude(), c.getLongitude());
            	    			newMarkerOpts.setPosition(user);
            	    			
            	    			//Custom icon from my Dropbox
            	    			newMarkerOpts.setIcon(MarkerImage.create("https://dl.dropboxusercontent.com/u/15430100/user.png"));
                        	    
                        	    newMarkerOpts.setMap(map);
                        	    newMarkerOpts.setTitle("Your Location");
                        	    Marker.create(newMarkerOpts);
            	    		}

							@Override
							public void onFailure(PositionError reason) {
								System.out.println(reason.getMessage());
							}
            	    	});
            	    }
            	    
            	    registerCallbacks();
                }
            });
    }
    
    public native void registerCallbacks() /*-{
    	// localThis captures "this" for reference inside the closure function.
    	var localThis = this;
    	$wnd['clearOverlays'] = function() {
      		$entry(localThis.@com.sneakyxpress.webapp.client.greeting.GreetingContent::clearOverlays()());
    	}
    	$wnd['showOverlays'] = function() {
      		$entry(localThis.@com.sneakyxpress.webapp.client.greeting.GreetingContent::showOverlays()());
    	}
  	}-*/;
    
    // Removes the overlays from the map, but keeps them in the array
    private void clearOverlays() {
    	marker.setMap((GoogleMap) null);
    }
    
    // shows the overlays from the map
    private void showOverlays() {
    	marker.setMap(map);
    }
}
