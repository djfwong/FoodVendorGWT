package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;

/**
 * Created by michael on 11/14/2013.
 */
public class AdministrationTab extends AbstractNavbarTab {
	private final Sneaky_Xpress module;

	public AdministrationTab(Sneaky_Xpress module) {
		this.module = module;
	}

	@Override
	public String getTitle()
	{
		return "Administration";
	}

	@Override
	public FlowPanel getContent()
	{
		FlowPanel admin = new FlowPanel();

		HTMLPanel info = null;

		info = new HTMLPanel(
				"<p class=\"lead pagination-centered\">Click on the Update Data button below to update to the latest food truck vendor data set.</p>");
		admin.add(info);

		admin.add(getButtonWidget(module.getUpdateDataButton()));
		return admin;
	}
}
