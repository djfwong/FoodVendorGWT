package com.sneakyxpress.webapp.client.pages.truckclaim;

import java.util.logging.Level;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.WatermarkedTextBox;
import com.sneakyxpress.webapp.client.pages.Content;

public class TruckClaimContent extends Content {

	private static final String pageName = "Claim Food Truck";
	private static final String pageStub = "claim";

	// A panel to hold messages in
	private FlowPanel messages = new FlowPanel();

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	

	private static final String NAME_PATTERN = "(\\w| )*";
	private static final String PHONE_PATTERN = "^[0-9]*$";

	private static final int STANDARD_MAX_FULL_NAME_LENGTH = 70;
	private static final int STANDARD_MAX_EMAIL_LENGTH = 70;
	private static final int STANDARD_PHONE_NUMBER_LENGTH = 10;

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
		//form.addStyleName("span6");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create TextBoxes, giving it a name so that it will be submitted.
		
		//Name textbox
		final WatermarkedTextBox nameBox = createTextBox("Name On Business License");
		nameBox.setName("nameBoxInput");

		//Email textbox
		final WatermarkedTextBox emailBox = createTextBox("Contact Email");
		emailBox.setName("emailBoxInput");

		//Phone textbox
		final WatermarkedTextBox phoneBox = createTextBox("Contact Number");
		phoneBox.setName("phoneBoxInput");
		
		//Terms and agreement checkbox
		final CheckBox checkTerms = new CheckBox("I have read and agree to the Terms of Service.");
		checkTerms.setValue(false);
		checkTerms.setWidth("400px");

		// Widget to set as form since can only add one widget to a form.
		VerticalPanel componentPanel = new VerticalPanel();

		// Add text boxes and checkbox
		componentPanel.add(nameBox);
		componentPanel.add(emailBox);
		componentPanel.add(phoneBox);
		componentPanel.add(checkTerms);
		
		componentPanel.add((makeLabelWidget("Select photo of business license")));

		// Create a FileUpload widget.
		final FileUpload upload = new FileUpload();
		upload.setName("uploadClaimFormElement");
		upload.setStyleName("btn btn-info");
		upload.setWidth("300px");
		componentPanel.add(upload);

		// Add a 'submit' button.
		final Button submitButton = new Button("Submit Request");
		submitButton.addStyleName("btn btn-primary");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.submit();
				// Disable button to disallow users to resubmit same form twice
				submitButton.setEnabled(false);
			}
		});
		

		// Add an event handler to the form. Form validation checks
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {

				String name = nameBox.getText().trim();
				String email = emailBox.getText().trim();
				String number = phoneBox.getText().trim();
				String fileName = upload.getFilename();

				// Check for empty strings
				if (name.length() == 0 || email.length() == 0
						|| number.length() == 0)
				{
					String errorMsg = "Sorry some of your inputs are empty. Please enter something.";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}
				
				// Check name length does not exceed 70
				else if (name.length() > STANDARD_MAX_FULL_NAME_LENGTH)
				{
					String errorMsg = "Sorry, " + name + " is too long. "
							+ "Please ensure your full name input is within "
							+ STANDARD_MAX_FULL_NAME_LENGTH + " characters";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}

				else if (!name.matches(NAME_PATTERN))
				{
					String errorMsg = "Sorry, \""
							+ name
							+ "\" contains invalid characters. "
							+ "Only letters and spaces are allowed. Please check your name input and try again.";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}
				
				// Check email length does not exceed 255
				else if (email.length() > STANDARD_MAX_EMAIL_LENGTH)
				{
					String errorMsg = "Sorry, " + email + " is too long. "
							+ "Please ensure your email input is within "
							+ STANDARD_MAX_EMAIL_LENGTH + " characters";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}

				else if (!email.matches(EMAIL_PATTERN))
				{
					String errorMsg = "Sorry, \"" + email
							+ "\" contains invalid characters. "
							+ "Please check your email input and try again.";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}

				// Check phone number input only contains numbers
				else if (!number.matches(PHONE_PATTERN))
				{
					String errorMsg = "Sorry, \""
							+ number
							+ "\" contains invalid characters. "
							+ "Only numbers are allowed. Please check your phone number input and try again.";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}

				// Phone number must have 10 digits
				else if (number.length() > STANDARD_PHONE_NUMBER_LENGTH
						|| number.length() < STANDARD_PHONE_NUMBER_LENGTH)
				{
					String errorMsg = "Please ensure your phone number input " + number + " contains "
							+ STANDARD_PHONE_NUMBER_LENGTH + " numbers.";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}
				
				else if(!checkTerms.getValue()){
					String errorMsg = "Sorry, please check off terms and agreement box and retry submitting your request again";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}
				
				else if(fileName.length() == 0){
					String errorMsg = "Sorry, you must select a picture of your business license from your local file system.";
					module.addMessage(true, errorMsg);
					addMessage(true, errorMsg);
					event.cancel();
				}

			}
		});
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this
				// event is
				// fired. Assuming the service returned a response of type
				// text/html,
				// we can get the result text here (see the FormPanel
				// documentation for
				// further explanation).
				module.addMessage(true, "Request submitted, if approved you will be contacted shortly");
				addMessage(true, event.getResults());
			}
		});

		componentPanel.add(getButtonWidget(submitButton));

		form.setWidget(componentPanel);
		content.add(form);

		// Change the content
		module.changeContent(form);
	}

	// Returns a text box with the specified watermark for phone number input
	public WatermarkedTextBox createTextBox(String watermark) {

		final WatermarkedTextBox input = new WatermarkedTextBox();

		input.setWatermark(watermark);
		
		input.setWidth("400px");

		return input;
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
	
	private HTMLPanel getButtonWidget(Button button) {
        HTMLPanel div = new HTMLPanel("<br>");
        div.add(button);
        return div;
    }
	
	private HTMLPanel makeLabelWidget(String text) {
        HTMLPanel div = new HTMLPanel("<br>");
        Label label = new Label(text);
        label.setStyleName("label label-info");
        div.add(label);
        return div;
    }
}
