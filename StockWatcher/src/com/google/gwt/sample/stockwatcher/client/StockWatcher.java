package com.google.gwt.sample.stockwatcher.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StockWatcher implements EntryPoint {
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private VerticalPanel firstPanel = new VerticalPanel();
	private VerticalPanel chatAreaPanel = new VerticalPanel();
	private VerticalPanel chatPanel = new VerticalPanel();

	private FlexTable employeeTable = new FlexTable();
	private Label employeeLabel = new Label("Enter employee name to see chats: ");
	private TextBox employeeTextBox = new TextBox();
	private Button employeeButton = new Button("View chats");
	private Label chatNonetext = new Label("No previous chats");

	private FlexTable chatTable = new FlexTable();
	private Label chatLabel = new Label("Enter label name to see chat: ");
	private TextBox chatTextBox = new TextBox();
	private Button chatButton = new Button("View chat details");

	private Label urlLabel = new Label();
	private Label employeechatLabel = new Label();
	private Label idLabel = new Label();
	private Label waitLabel = new Label();
	private TextArea chatTextArea = new TextArea();

	private DataAccessServiceAsync dataAccessSvc = GWT.create(DataAccessService.class);

	private static final Logger log = Logger.getLogger(StockWatcher.class.getName());

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {

		employeeTable.setText(0, 0, "Employee");

		if (dataAccessSvc == null) {
			dataAccessSvc = GWT.create(DataAccessService.class);
		}

		AsyncCallback<String> callback_employee = new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				String[] employees = result.split("\n");
				for (int i = 0; i < employees.length; i++) {
					int row = employeeTable.getRowCount();
					employeeTable.setText(row, 0, employees[i]);

				}
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};

		dataAccessSvc.getEmployees(callback_employee);

		firstPanel.add(employeeTable);
		firstPanel.add(employeeLabel);
		firstPanel.add(employeeTextBox);
		firstPanel.add(employeeButton);
		System.out.println("first panel added");

		// Add styles to elements in the chat stat table.
		chatTable.setCellPadding(6);

		chatTextArea.setCharacterWidth(50);
		chatTextArea.setVisibleLines(50);

		chatPanel.add(chatNonetext);
		chatPanel.add(chatTable);
		chatPanel.add(chatLabel);
		chatPanel.add(chatTextBox);
		chatPanel.add(chatButton);

		chatAreaPanel.add(idLabel);
		chatAreaPanel.add(urlLabel);
		chatAreaPanel.add(employeechatLabel);
		chatAreaPanel.add(waitLabel);

		chatAreaPanel.add(chatTextArea);

		chatPanel.setVisible(false);
		chatAreaPanel.setVisible(false);
		mainPanel.add(firstPanel);
		mainPanel.add(chatPanel);
		mainPanel.add(chatAreaPanel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("chatStats").add(mainPanel);

		// Listen for mouse events on the Add button.
		employeeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				log.info("Inside click evennt");

				populateTable();

			}
		});

		// Listen for keyboard events in the input box.
		employeeTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

					populateTable();
				}
			}
		});

		// Listen for mouse events on the Add button.
		chatButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getChatDetails();

			}
		});

		// Listen for keyboard events in the input box.
		chatTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					getChatDetails();
				}
			}
		});

	}

	private void setTable() {
		// Create table for chat.
		chatTable.setText(0, 0, "Label");
		chatTable.setText(0, 1, "Chat ID");
		chatTable.setText(0, 2, "Date");
		chatTable.setText(0, 3, "WaitTime");

		chatTable.getRowFormatter().addStyleName(0, "watchListHeader");
		chatTable.addStyleName("watchList");
		chatTable.getCellFormatter().addStyleName(0, 1, "watchListTextColumn");
		chatTable.getCellFormatter().addStyleName(0, 2, "watchListTextColumn");
		chatTable.getCellFormatter().addStyleName(0, 3, "watchListTextColumn");

	}

	private void getChatDetails() {
		final String input = chatTextBox.getText();
		employeeTextBox.setFocus(true);
		chatAreaPanel.setVisible(true);

		AsyncCallback<String> callback_chatarea = new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub

				String[] chats = result.split(";");
				idLabel.setText("Chat ID :" + chats[0]);
				employeechatLabel.setText("Employee ID :" + chats[1]);
				waitLabel.setText("Wait Time :" + chats[2] + "s");
				urlLabel.setText("ChatURL :" + chats[3]);

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};

		dataAccessSvc.getChatDetails(input, callback_chatarea);

		AsyncCallback<String> callback_chatdata = new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub

				chatTextArea.setText(result);

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};

		dataAccessSvc.getChatData(input, callback_chatdata);
	}

	/**
	 * Add chats to Chat Table. Executed when the user clicks the addStockButton
	 * or presses enter in the newSymbolTextBox.
	 */
	private void populateTable() {
		final String input = employeeTextBox.getText();
		employeeTextBox.setFocus(true);

		log.info("Inside populate");
		chatPanel.setVisible(true);
		chatAreaPanel.setVisible(false);
		AsyncCallback<String> callback_chattable = new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				chatTable.removeAllRows();
				if (result.equals("x")) {
					chatTable.setVisible(false);
					chatButton.setVisible(false);
					chatTextBox.setVisible(false);
					chatLabel.setVisible(false);
					chatNonetext.setVisible(true);

				} else {
					String[] chats = result.split("\n");
					chatTable.setVisible(true);
					chatButton.setVisible(true);
					chatTextBox.setVisible(true);
					chatLabel.setVisible(true);
					chatNonetext.setVisible(false);
					setTable();
					for (int i = 0; i < chats.length; i++) {
						String[] details = chats[i].split(";");
						System.out.println(details);
						int row = chatTable.getRowCount();
						for (int j = 0; j < 4; j++) {
							chatTable.setText(row, j, details[j]);
						}
					}
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};

		dataAccessSvc.getChatsByEmployee(input, callback_chattable);

	}

}
