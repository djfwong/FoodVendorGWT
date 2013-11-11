package com.sneakyxpress.webapp.client.facebook;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

class FBFeed extends JavaScriptObject {
	protected FBFeed() {
	}

	public final native JsArray<FBEntry> getData() /*-{
		return this.data;
	}-*/;
}