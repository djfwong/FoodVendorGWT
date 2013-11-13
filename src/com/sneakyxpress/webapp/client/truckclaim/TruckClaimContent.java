package com.sneakyxpress.webapp.client.truckclaim;

import java.util.logging.Level;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sneakyxpress.webapp.client.Content;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.WatermarkedTextBox;

public class TruckClaimContent extends Content {

	private static final String pageName = "Claim Food Truck";
	private static final String pageStub = "claim";

	// A panel to hold messages in
	private FlowPanel messages = new FlowPanel();

	public TruckClaimContent(Sneaky_Xpress module) {
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
		HTMLPanel content = new HTMLPanel(""); // The base panel to hold all
		// content

		// Truck verification form
		FormPanel form = new FormPanel("");
		form.addStyleName("span6");

		// Add name text box
		WatermarkedTextBox nameBox = textInputTextBox("Name On Business License");
		WatermarkedTextBox emailBox = textInputTextBox("Contact Email");
		WatermarkedTextBox phoneBox = numberTextBox("Contact Number");

		// Widget to set as form since can only add one widget to a form.
		VerticalPanel vertPanel = new VerticalPanel();

		// Add text boxes
		vertPanel.add(nameBox);
		vertPanel.add(emailBox);
		vertPanel.add(phoneBox);

		form.setWidget(vertPanel);

		content.add(form);

		// Change the content
		module.changeContent(form);
	}

	public void addMessage(boolean error, String info) {
		// Create & add the message
		String type = error ? "alert-danger" : "alert-info";
		HTML alert = new HTML(
				"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">"
						+ "&times;</button>" + info);
		alert.addStyleName("alert " + type + " fade");
		messages.insert(alert, 0); // Add the message to the top
		alert.addStyleName("in");

		logger.log(Level.INFO, "addMessage: " + info);
	}

	// Returns a text box with the specified watermark for phone number input
	public WatermarkedTextBox numberTextBox(String watermark) {
		final WatermarkedTextBox numberInput = new WatermarkedTextBox();
		numberInput.setWatermark(watermark);

		numberInput.setWidth("400px");

		numberInput.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String input = numberInput.getText().trim(); // Remove extra
					// whitespace

					if (!input.matches("^[0-9]*$")) {
						addMessage(
								true,
								"Sorry, \""
										+ input
										+ "\" contains invalid characters. "
										+ "Only numbers are allowed. Please remove them and try again.");
					} else if (input.length() > 10) {
						addMessage(true, "Sorry, your input is too long. "
								+ "Please limit your input to 10 numbers.");
					} else if (input.length() == 0) {
						addMessage(true,
								"Sorry your input is empty. Please enter something.");
					}
				}
			}
		});
		return numberInput;
	}

	// Returns a text box with the specified watermark for text input
	public WatermarkedTextBox textInputTextBox(String watermark) {
		final WatermarkedTextBox textInputBox = new WatermarkedTextBox();
		textInputBox.setWatermark(watermark);

		textInputBox.setWidth("400px");

		textInputBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String input = textInputBox.getText().trim(); // Remove
					// extra
					// whitespace

					if (!input.matches("(\\w| )*")) {
						addMessage(
								true,
								"Sorry, \""
										+ input
										+ "\" contains invalid characters. "
										+ "Only letters and spaces are allowed. Please remove them and try again.");
					} else if (input.length() > 20) {
						addMessage(true, "Sorry, your input is too long. "
								+ "Please limit your input to 20 characters.");
					} else if (input.length() == 0) {
						addMessage(true,
								"Sorry your input is empty. Please enter something.");
					}
				}
			}
		});
		return textInputBox;
	}
}
