package com.sneakyxpress.webapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
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
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

/**
 * Get the content for our home page.
 */
public class GreetingContent extends Content {
    private static final String pageName = "Vancouver Food Vendor Reviews";
    private static final String pageStub = "home";

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
                    module.addMessage(GENERIC_ERROR_MESSAGE);
                }

                public void onSuccess(String result) {
                    module.changeContent(new HTML(result));
                    
                    LatLng myLatLng = LatLng.create(49.250, -123.100);
            	    MapOptions myOptions = MapOptions.create();
            	    myOptions.setZoom(12.0);
            	    myOptions.setCenter(myLatLng);
            	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
            	    final GoogleMap map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
            	    
            	    InfoWindowOptions infowindowOpts = InfoWindowOptions.create();
            	    infowindowOpts.setContent("test");
            	    final InfoWindow infowindow = InfoWindow.create(infowindowOpts);
            	    
            	    MarkerOptions newMarkerOpts = MarkerOptions.create();
            	    newMarkerOpts.setPosition(myLatLng);
            	    newMarkerOpts.setMap(map);
            	    newMarkerOpts.setTitle("Hello World!");
            	    final Marker marker = Marker.create(newMarkerOpts);
            	    
            	    marker.addClickListener(new ClickHandler() {
            	    	public void handle(MouseEvent event) {
            	    		infowindow.open(map, marker);
            	    	}
            	    });
                }
            });
    }
}
