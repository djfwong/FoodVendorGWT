package com.sneakyxpress.webapp.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sneakyxpress.webapp.shared.FoodVendor;

/**
 * The contents of the Browse Vendors page
 */
public class BrowseVendorsContent extends Content {
	private static final String pageName = "Browse Vendors";
	private static final String pageStub = "browse";

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
						module.addMessage(GENERIC_ERROR_MESSAGE);
					}

					public void onSuccess(List<FoodVendor> result) {
						HTMLPanel content = new HTMLPanel(""); // The new
																// content to
																// return

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
						} else {
							// By default load the list view
							listView.addStyleName("active");
							mapView.removeStyleName("active");

							// Add data table under List view
							Widget table = new FoodVendorDisplayTable(result).getWidget();
							//Widget table = displayDataInTable(result);
							content.add(table);
						}

						module.changeContent(content);
					}
				});
	}	
}


