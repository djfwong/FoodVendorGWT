package com.sneakyxpress.webapp.client.facebook;

import com.google.gwt.core.client.JavaScriptObject;

class FBEntry extends JavaScriptObject {
	protected FBEntry() {
	}

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getId() /*-{
		return this.id;
	}-*/;
}