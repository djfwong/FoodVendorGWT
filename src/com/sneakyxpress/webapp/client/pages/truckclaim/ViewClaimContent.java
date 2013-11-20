package com.sneakyxpress.webapp.client.pages.truckclaim;

import java.util.logging.Level;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.Content;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserService;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserServiceAsync;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorService;
import com.sneakyxpress.webapp.client.services.verifiedvendorservice.VerifiedVendorServiceAsync;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

public class ViewClaimContent extends Content {

	private static final String pageName = "View Claim";
	private static final String pageStub = "viewclaim";

	// A panel to hold messages in
	private FlowPanel messages = new FlowPanel();

	// TODO
	// Need a way to pull ids (from page input)?
	String fbId;
	String truckId;

	public ViewClaimContent(Sneaky_Xpress module) {
		super(module);
	}

	private final ClaimServiceAsync claimsService = GWT
			.create(ClaimService.class);

	private final PersistUserServiceAsync userService = GWT
			.create(PersistUserService.class);

	private final VerifiedVendorServiceAsync verifiedVendorService = GWT
			.create(VerifiedVendorService.class);

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
	public void getAndChangeContent(String input)
	{
		
		HTMLPanel content = new HTMLPanel("");
		HTMLPanel panel = new HTMLPanel("");

		// Accept Claim button
		Button acceptButton = new Button("Accept");
		acceptButton.addStyleName("btn btn-success");
		acceptButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				claimsService.acceptClaim(fbId, truckId,
						new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught)
					{
						logger.log(Level.SEVERE, caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result)
					{
						logger.log(Level.INFO,
								"claimsService.acceptClaim: " + result);

						if (result)
						{
							module.addMessage(false, "Claim accepted.");
						}
						else
						{
							module.addMessage(true,
									"Error in accepting claim.");
						}
					}

				});

				module.addMessage(false, "Claim status set to accepted");
			}
		});

		panel.add(getButtonWidget(acceptButton));
		content.add(panel);

		Button rejectButton = new Button("Reject");
		rejectButton.addStyleName("btn btn-danger");
		rejectButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				claimsService.rejectClaim(fbId, truckId,
						new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught)
					{
						logger.log(Level.SEVERE, caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result)
					{
						logger.log(Level.INFO,
								"claimsService.rejectClaim: " + result);

						if (result)
						{
							module.addMessage(false, "Claim rejected.");
						}
						else
						{
							module.addMessage(true,
									"Error in rejecting claim.");
						}
					}

				});

				module.addMessage(false, "Claim status set to rejected");
			}
		});

		panel.add(getButtonWidget(rejectButton));
		content.add(panel);
		module.changeContent(content);
	}

	// Creates button with space in between panels
	private HTMLPanel getButtonWidget(Button button)
	{
		HTMLPanel div = new HTMLPanel("<br>");
		div.add(button);
		return div;
	}

	// TODO
	// Update VerifiedVendor table and check a truckID is not already there

	// Change role of user to owner
	public void updateUserRole()
	{
		userService.changeUserStatus(fbId, 2, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught)
			{
				logger.log(Level.SEVERE, caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result)
			{
				logger.log(Level.INFO, "userService.changeUserStatus: "
						+ result);

				if (result)
				{
					module.addMessage(false, "Updated User Role");
				}
				else
				{
					module.addMessage(true, "Error in updating user role.");
				}
			}

		});

	}

	public void addToVerifiedVendors()
	{
		VerifiedVendor v = new VerifiedVendor();
		v.setId(fbId);
		v.setVendorId(truckId);

		verifiedVendorService.addVerifiedVendor(v,
				new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught)
			{
				logger.log(Level.SEVERE, caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result)
			{
				if (result)
				{
					module.addMessage(false,
							"Updated Truck As Verified");
				}
				else
				{
					module.addMessage(true,
							"Error in verifying truck, could be already claimed");
				}
			}

		});

	}

	// Logs error messages
	public void addMessage(boolean error, String info)
	{
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
}