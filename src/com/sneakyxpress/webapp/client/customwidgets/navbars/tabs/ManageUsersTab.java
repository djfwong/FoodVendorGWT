package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.DeleteButton;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserService;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserServiceAsync;
import com.sneakyxpress.webapp.shared.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 11/20/2013.
 */
public class ManageUsersTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;

    public ManageUsersTab(Sneaky_Xpress module) {
        this.module = module;
    }

    @Override
    public String getTitle() {
        return "Manage Users";
    }

    @Override
    public FlowPanel getContent() {
        final FlowPanel content = new FlowPanel();

        PersistUserServiceAsync persistUserService = GWT.
                create(PersistUserService.class);
        persistUserService.getAllUsers(new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {
                module.addMessage(true, "Error loading application users. Reason: " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<User> result) {
                if (result == null) {
                    HTMLPanel response = new HTMLPanel("p", "No users could be found :(");
                    response.addStyleName("lead pagination-centered");
                    content.add(response);
                    content.addStyleName("well");
                } else {
                    SimpleTable userTable = new SimpleTable("table-hover table-bordered",
                            "Id", "Name", "Type", "Email");

                    List<DeleteButton> buttons = new LinkedList<DeleteButton>();
                    for (User u : result) {
                        userTable.addRow(new PageClickHandler(module.PROFILE_PAGE, u.getId()),
                                u.getId(), u.getName(), u.getTypeName(), u.getEmail());
                        buttons.add(new DeleteButton(u.getId()));
                    }

                    // Create the delete button
                    Button button = new Button("Delete Selected");
                    button.addClickHandler(new DeleteUsersClickHandler(buttons));

                    userTable.addWidgetColumn(button, buttons);

                    userTable.sortRows(0, false);

                    content.add(userTable);
                }
            }
        });

        return content;
    }

    private class DeleteUsersClickHandler implements ClickHandler {
        private final List<DeleteButton> buttons;
        public DeleteUsersClickHandler(List<DeleteButton> buttons) {
            this.buttons = buttons;
        }

        @Override
        public void onClick(ClickEvent event) {
            HTMLPanel areYouSure = new HTMLPanel("<p class=\"lead pagination-centered\">"
                    + "Are you sure?</p>");
            Button confirm = new Button("Confirm Deletion");
            confirm.addStyleName("btn btn-danger btn-large btn-block");

            areYouSure.add(confirm);
            final Modal modal = module.addModal("Confirm Deletion", areYouSure);

            confirm.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    module.addMessage(false, "Fake deleting users");
                    // TODO: Add delete users service ehre
                    modal.hide();
                }
            });
        }
    }
}
