package com.sneakyxpress.webapp.client.truckclaim;

import java.util.logging.Level;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
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
		form.add(businessNameForm());

		content.add(form);

		// Change the content
		module.changeContent(content);
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

	public TextBox businessNameForm() {
		final WatermarkedTextBox nameInput = new WatermarkedTextBox();
		nameInput.setWatermark("Name on License");
		
		nameInput.setWidth("230px");

		nameInput.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String input = nameInput.getText().trim(); // Remove extra
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
		return nameInput;
	}
}
