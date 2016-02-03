package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DataAccessServiceAsync {
	void getChatsByEmployee(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getChatsByDate(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getChatDetails(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getChatData(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getEmployees(AsyncCallback<String> callback) throws IllegalArgumentException;
	void getStatistics(AsyncCallback<String> callback) throws IllegalArgumentException;
}
