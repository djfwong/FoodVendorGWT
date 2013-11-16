package com.sneakyxpress.webapp.server;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.sneakyxpress.webapp.shared.TruckClaim;

public class ClaimFormServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		ServletFileUpload upload = new ServletFileUpload();
		TruckClaim truckClaim = new TruckClaim();

		try
		{
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext())
			{

				FileItemStream item = iter.next();

				if (item.isFormField())
				{
					if (item.getFieldName().equals("fbId"))
					{
						truckClaim.setFacebookId(Streams.asString(item
								.openStream()));
					}
					else if (item.getFieldName().equals("vendorId"))
					{
						truckClaim.setTruckId(Streams.asString(item
								.openStream()));
					}
					else if (item.getFieldName().equals("nameBoxInput"))
					{
						truckClaim.setName(Streams.asString(item.openStream()));
					}
					else if (item.getFieldName().equals("emailBoxInput"))
					{
						truckClaim
								.setEmail(Streams.asString(item.openStream()));
					}
					else if (item.getFieldName().equals("phoneBoxInput"))
					{
						truckClaim.setPhoneNumber(Streams.asString(item
								.openStream()));
					}
					else if (item.getFieldName().equals("phoneBoxInput"))
					{
						truckClaim.setPhoneNumber(Streams.asString(item
								.openStream()));
					}

				}
				else
				{
					System.out.println("Not a form field");
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public void persistClaimData(TruckClaim claim)
	{
		// Persist truck claim data
		PersistenceManager pm = PMF.get().getPersistenceManager();

		pm.makePersistent(claim);

		pm.close();
	}
}
