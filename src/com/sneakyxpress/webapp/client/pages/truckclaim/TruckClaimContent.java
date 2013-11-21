package com.sneakyxpress.webapp.client.pages.truckclaim;

import java.util.logging.Level;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.WatermarkedTextBox;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.pages.greeting.GreetingContent;

public class TruckClaimContent extends Content {

	// Umm this is a long string sorry about that
	private static final String TERMS_OF_SERVICE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse justo mi, ultricies non lorem ut, tristique convallis tellus. In et egestas odio. Aliquam erat volutpat. Quisque diam metus, sollicitudin vitae eros a, malesuada consequat est. Praesent arcu eros, ultricies non tincidunt et, mattis at mauris. Duis lacinia pharetra nunc nec suscipit. Cras sit amet diam et diam luctus scelerisque. Nullam accumsan metus vel nunc condimentum, non dapibus nunc facilisis. Ut porta, mi tincidunt congue congue, ante ante gravida mi, eu volutpat nulla ligula at orci. In hac habitasse platea dictumst. In metus augue, fermentum a lectus eu, sollicitudin posuere magna. Etiam eleifend, est vitae sagittis dignissim, eros sapien consequat nisl, quis facilisis nunc neque tincidunt lorem. Praesent sapien velit, pulvinar vel interdum at, faucibus non magna.\n"
			+ "\n"
			+ "Nullam ac cursus libero. Pellentesque malesuada posuere ipsum, nec accumsan augue faucibus vel. Aliquam nunc dolor, varius ac pellentesque eget, volutpat in justo. Fusce congue lectus elit, non pharetra sapien sagittis ac. Fusce rutrum massa varius orci lacinia mollis. Pellentesque semper sem sed enim pulvinar viverra. Cras nec dui quam. ";

	private static final String pageName = "Claim Food Truck";
	private static final String pageStub = "claim";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String NAME_PATTERN = "(\\w| )*";
	private static final String PHONE_PATTERN = "^[0-9]*$";
	private static final int STANDARD_MAX_FULL_NAME_LENGTH = 70;
	private static final int STANDARD_MAX_EMAIL_LENGTH = 70;
	private static final int STANDARD_PHONE_NUMBER_LENGTH = 10;
	private WatermarkedTextBox vendorIdBox;
    private WatermarkedTextBox fbIdBox;
    private WatermarkedTextBox nameBox;
    private WatermarkedTextBox emailBox;
    private WatermarkedTextBox phoneBox;
    private CheckBox checkTerms;
    private Button submitButton;

	// Declare form
    private FormPanel form;


	public TruckClaimContent(Sneaky_Xpress module) {
		super(module);

		// Create a FormPanel
		form = new FormPanel();
	}

	@Override
	public String getPageName()
	{
		return pageName;
	}

	@Override
	public String getPageStub()
	{
		return pageStub;
	}

	@Override
	public void getAndChangeContent(final String input)
	{

		HTMLPanel content = new HTMLPanel(""); // The base panel to hold all
												// content
		content.addStyleName("row-fluid");

		// The left side will hold the form
		HTMLPanel leftSide = new HTMLPanel("");
		leftSide.addStyleName("span6");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		System.out.println(GWT.getModuleBaseURL());
		form.setAction(GWT.getModuleBaseURL() + "claimFormReq");

		// Create TextBoxes, giving it a name so that it will be submitted.
		// Vendor Id textbox - used to add to form
		vendorIdBox = createTextBox("Vendor Key");
		vendorIdBox.setName("vendorId");
		vendorIdBox.setVisible(false);
		vendorIdBox.setText(input);

		// Facebook Id textbox - used to add to form
		fbIdBox = createTextBox("Vendor Key");
		fbIdBox.setName("fbId");
		fbIdBox.setVisible(false);

		// Name textbox
		nameBox = createTextBox("Name On Business License");
		nameBox.setName("nameBoxInput");
		nameBox.setStyleName("input-block-level");

		// Email textbox
		emailBox = createTextBox("Contact Email");
		emailBox.setName("emailBoxInput");
		emailBox.setStyleName("input-block-level");

		// Phone textbox
		phoneBox = createTextBox("Contact Number");
		phoneBox.setName("phoneBoxInput");
		phoneBox.setStyleName("input-block-level");

		// Terms and agreement checkbox
		checkTerms = new CheckBox(
				"I have read and agree to the Terms of Service.");
		checkTerms.setName("checkTerms");
		checkTerms.setValue(false);
		checkTerms.setWidth("400px");

		// Widget to set as form since can only add one widget to a form.
		VerticalPanel componentPanel = new VerticalPanel();

		// Add text boxes and checkbox
		componentPanel.add(fbIdBox);
		componentPanel.add(vendorIdBox);
		componentPanel.add(nameBox);
		componentPanel.add(emailBox);
		componentPanel.add(phoneBox);
		componentPanel.add(checkTerms);

		// Add a 'submit' button.
		submitButton = new Button("Submit Request");
		submitButton.addStyleName("btn btn-primary");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				form.submit();
			}
		});

		// Add an event handler to the form. Form validation checks
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event)
			{
				String name = nameBox.getText().trim();
				String email = emailBox.getText().trim();
				String number = phoneBox.getText().trim();
				String facebookId = "";

				// Check to make sure logged in user is making request
				if (!FacebookTools.isLoggedIn())
				{
					String errorMsg = "Sorry you're not logged in. Login or register a new account. ";
					module.addMessage(true, errorMsg);

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}

				// Check for empty strings
				else if (name.length() == 0 || email.length() == 0
						|| number.length() == 0)
				{
					String errorMsg = "Sorry some of your inputs are empty. Please enter something.";
					module.addMessage(true, errorMsg);

					// if/else to detect and find a bug (can't submit multiple
					// forms)
					if (name.length() == 0)
					{
						logger.log(
								Level.INFO,
								"Name Empty. Contents of TextBox: "
										+ nameBox.getText());
						nameBox.setFocus(true);
					}
					else if (email.length() == 0)
					{
						logger.log(
								Level.INFO,
								"Email Empty. Contents of TextBox: "
										+ emailBox.getText());
						emailBox.setFocus(true);
					}
					else if (number.length() == 0)
					{
						logger.log(Level.INFO,
								"Number Empty. Contents of TextBox: "
										+ phoneBox.getText());
						phoneBox.setFocus(true);
					}
					else
					{
						logger.log(Level.SEVERE, "Unfound bug");
					}

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}

				// Check name length does not exceed 70
				else if (name.length() > STANDARD_MAX_FULL_NAME_LENGTH)
				{
					String errorMsg = "Sorry, " + name + " is too long. "
							+ "Please ensure your full name input is within "
							+ STANDARD_MAX_FULL_NAME_LENGTH + " characters";
					module.addMessage(true, errorMsg);

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}
				else if (!name.matches(NAME_PATTERN))
				{
					String errorMsg = "Sorry, \""
							+ name
							+ "\" contains invalid characters. "
							+ "Only letters and spaces are allowed. Please check your name input and try again.";
					module.addMessage(true, errorMsg);

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}

				// Check email length does not exceed 255
				else if (email.length() > STANDARD_MAX_EMAIL_LENGTH)
				{
					String errorMsg = "Sorry, " + email + " is too long. "
							+ "Please ensure your email input is within "
							+ STANDARD_MAX_EMAIL_LENGTH + " characters";
					module.addMessage(true, errorMsg);

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}
				else if (!email.matches(EMAIL_PATTERN))
				{
					String errorMsg = "Sorry, \"" + email
							+ "\" contains invalid characters. "
							+ "Please check your email input and try again.";
					module.addMessage(true, errorMsg);

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
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

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}

				// Phone number must have 10 digits
				else if (number.length() > STANDARD_PHONE_NUMBER_LENGTH
						|| number.length() < STANDARD_PHONE_NUMBER_LENGTH)
				{
					String errorMsg = "Please ensure your phone number input "
							+ number + " contains "
							+ STANDARD_PHONE_NUMBER_LENGTH + " numbers.";
					module.addMessage(true, errorMsg);

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}
				else if (!checkTerms.getValue())
				{
					String errorMsg = "Sorry, please check off terms and agreement box and retry submitting your request again";
					module.addMessage(true, errorMsg);

					// Re-enable button to allow user to resubmit form with
					// changes
					submitButton.setEnabled(true);
					event.cancel();
				}
				else
				{
					// All checks passed, grab user's ID
					facebookId = FacebookTools.getUserId();
					fbIdBox.setText(facebookId);

					// Disable button to disallow users to resubmit same form
					// twice
					submitButton.setEnabled(false);
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event)
			{
				// When the form submission is successfully completed, this
				// event is
				// fired. Assuming the service returned a response of type
				// text/html,
				// we can get the result text here (see the FormPanel
				// documentation for
				// further explanation).

				if (event.getResults().equals("true"))
				{
					// Display message to user
					module.addMessage(false,
							"Your request has been submitted to our administrators.");
				}
				else
				{
					module.addMessage(
							true,
							"Error in submitting your request in submitting your requestion. you may have sent a claim form for this truck already.");
				}

				// Clear form
				form.reset();

				// Re-direct to home page since it's faster than re-directing to
				// user profile page
				History.newItem(new GreetingContent(module).getPageStub());
			}
		});

		componentPanel.add(getButtonWidget(submitButton));

		form.setWidget(componentPanel);
		leftSide.add(form);

		// The right side will hold the ToS
		HTMLPanel rightSide = new HTMLPanel(
				"<p class=\"well\"><strong>Terms of Service:</strong> "
						+ TERMS_OF_SERVICE + "</p>");
		rightSide.addStyleName("span6");

		// Put it all together
		content.add(leftSide);
		content.add(rightSide);

		// Change the content
		module.changeContent(content);
	}

	// Returns a text box with the specified watermark for phone number input
	public WatermarkedTextBox createTextBox(String watermark)
	{

		final WatermarkedTextBox input = new WatermarkedTextBox();

		input.setWatermark(watermark);

		input.setWidth("400px");

		return input;
	}

	// Creates button with space in between panels
	private HTMLPanel getButtonWidget(Button button)
	{
		HTMLPanel div = new HTMLPanel("<br>");
		div.add(button);
		return div;
	}
}
