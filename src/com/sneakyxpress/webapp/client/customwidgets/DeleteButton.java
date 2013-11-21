package com.sneakyxpress.webapp.client.customwidgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserService;

/**
 * Created by michael on 11/20/2013.
 */
public class DeleteButton extends Composite {
    public String getUserId()
	{
		return userId;
	}

	private final String userId;
    private boolean selected = false;
    private final Button button;

    public DeleteButton(String userId) {
        this.userId = userId;

        button = new Button("<i class=\"icon-trash\"></i>");
        button.addStyleName("btn");
        initWidget(button);

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selected = !selected;
                updateStyle();
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }

    private void updateStyle() {
        if (selected) {
            button.addStyleName("btn-danger");
        } else {
            button.removeStyleName("btn-danger");
        }
    }
}
