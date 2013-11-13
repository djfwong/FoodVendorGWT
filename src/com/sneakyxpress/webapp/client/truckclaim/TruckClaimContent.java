package com.sneakyxpress.webapp.client.truckclaim;

import java.util.logging.Level;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FileUpload;
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

		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel("");
		form.setAction("/claimFormHandler");
		form.addStyleName("span6");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create TextBoxes, giving it a name so that it will be submitted.
		WatermarkedTextBox nameBox = textInputTextBox("Name On Business License");
		nameBox.setName("nameBoxInput");

		WatermarkedTextBox emailBox = textInputTextBox("Contact Email");
		emailBox.setName("emailBoxInput");

		WatermarkedTextBox phoneBox = numberTextBox("Contact Number");
		phoneBox.setName("phoneBoxInput");

		// Widget to set as form since can only add one widget to a form.
		VerticalPanel vertPanel = new VerticalPanel();

		// Add text boxes
		vertPanel.add(nameBox);
		vertPanel.add(emailBox);
		vertPanel.add(phoneBox);

		// Create a FileUpload widget.
		FileUpload upload = new FileUpload();
		upload.setName("uploadClaimFormElement");
		upload.setWidth("400px");
		vertPanel.add(upload);

		// Add a 'submit' button.
	    Button submitButton = new Button("DONE");
	    submitButton.addStyleName("btn btn-primary btn-lg");
	    submitButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				form.submit();			
			}
	    });

	    vertPanel.add(submitButton);

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
