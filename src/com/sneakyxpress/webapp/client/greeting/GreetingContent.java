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
                }
            });
    }
}
