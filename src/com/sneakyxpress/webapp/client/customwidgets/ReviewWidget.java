package com.sneakyxpress.webapp.client.customwidgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.shared.VendorFeedback;

/**
 * Created by michael on 11/19/2013.
 */
public class ReviewWidget extends Composite {
    private final VendorFeedback f;

    public ReviewWidget(VendorFeedback f) {
        this.f = f;

        String stars = " ";
        for (int i = 0; i < f.getRating(); i++) {
            stars = "<i class=\"icon-star\"></i>" + stars;
        }

        HTMLPanel quote = new HTMLPanel("blockquote", "<p>" + stars
                + f.getReview() + "</p>");
        initWidget(quote);

        String vendorName = f.getVendorName();
        if (vendorName.isEmpty()) {
            vendorName = f.getVendorId();
        }

        quote.add(new HTMLPanel("small", f.getAuthorName() + " reviewing "
                + vendorName));
    }
}
