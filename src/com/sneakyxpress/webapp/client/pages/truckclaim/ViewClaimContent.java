package com.sneakyxpress.webapp.client.pages.truckclaim;

import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.pages.Content;

public class ViewClaimContent extends Content{
	
	private static final String pageName = "View Claim";
	private static final String pageStub = "viewclaim";

	public ViewClaimContent(Sneaky_Xpress module) {
		super(module);
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
	public void getAndChangeContent(String input)
	{
		// TODO Auto-generated method stub
		
	}
	
}