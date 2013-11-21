package com.sneakyxpress.webapp.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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
	protected static final Logger logger = Logger.getLogger("");

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		ServletFileUpload upload = new ServletFileUpload();
		TruckClaim truckClaim = new TruckClaim();

		try
		{
			response.setContentType("text/html");
			FileItemIterator iter = upload.getItemIterator(request);

			if (!iter.hasNext())
			{
				response.getWriter().write("Empty form submitted");
				return;
			}

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
					return;
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		response.getWriter().write(String.valueOf(persistClaimData(truckClaim)));
		response.getWriter().close();
	}

	public boolean persistClaimData(TruckClaim claim)
	{
		try
		{
			// Persist truck claim data
			PersistenceManager pm = PMF.get().getPersistenceManager();

			Query q = pm.newQuery(TruckClaim.class);
			q.setFilter("facebookId == :fbid && truckId == :truckid");

			Map<String, String> paramValues = new HashMap<String, String>();
			paramValues.put("fbid", claim.getFacebookId());
			paramValues.put("truckid", claim.getTruckId());

			@SuppressWarnings("unchecked")
			List<TruckClaim> claims = (List<TruckClaim>) q
					.executeWithMap(paramValues);

			if (claims.size() == 0)
			{
				// Check Same FB Id and Truck Id is not already in datastore
				pm.makePersistent(claim);

				pm.close();
				return true;
			}

			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "persistClaimData: " + e.getMessage());
			return false;
		}
	}

}
