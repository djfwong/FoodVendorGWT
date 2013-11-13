package com.sneakyxpress.webapp.client.customwidgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

public class WatermarkedTextBox extends TextBox implements BlurHandler,
		FocusHandler {
	String watermark;
	HandlerRegistration blurHandler;
	HandlerRegistration focusHandler;

	public WatermarkedTextBox() {
		super();
		this.setStylePrimaryName("textInput");
	}

	public WatermarkedTextBox(String defaultValue) {
		this();
		setText(defaultValue);
	}

	public WatermarkedTextBox(String defaultValue, String watermark) {
		this(defaultValue);
		setWatermark(watermark);
	}

	/**
	 * Adds a watermark if the parameter is not NULL or EMPTY
	 * 
	 * @param watermark
	 */
	public void setWatermark(final String watermark) {
		this.watermark = watermark;

		if ((watermark != null) && (watermark != "")) {
			blurHandler = addBlurHandler(this);
			focusHandler = addFocusHandler(this);
			EnableWatermark();
		} else {
			// Remove handlers
			blurHandler.removeHandler();
			focusHandler.removeHandler();
		}
	}

	@Override
	public void onBlur(BlurEvent event) {
		EnableWatermark();
	}

	void EnableWatermark() {
		String text = getText();
		if ((text.length() == 0) || (text.equalsIgnoreCase(watermark))) {
			// Show watermark
			setText(watermark);
			addStyleDependentName("watermark");
		}
	}

	@Override
	public void onFocus(FocusEvent event) {
		removeStyleDependentName("watermark");

		if (getText().equalsIgnoreCase(watermark)) {
			// Hide watermark
			setText("");
		}
	}

	// To detect empty inputs
	@Override
	public String getText() {
		String t = super.getText();
		if (t.equals(watermark))
			return "";

		return t;
	}

	// To detect empty inputs
	@Override
	public void setText(String text) {
		if (text == null)
			return;
		if (text.length() != 0)
			super.setText(text);
	}
}