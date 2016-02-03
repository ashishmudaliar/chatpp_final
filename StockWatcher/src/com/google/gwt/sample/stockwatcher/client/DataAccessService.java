package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("accesschatdata")
public interface DataAccessService extends RemoteService {
	String getChatsByEmployee(String name) throws IllegalArgumentException;
	String getChatsByDate(String name) throws IllegalArgumentException;
	
	String getChatDetails(String name) throws IllegalArgumentException;
	String getChatData(String name) throws IllegalArgumentException;
	
	String getEmployees() throws IllegalArgumentException;
	String getStatistics() throws IllegalArgumentException;
}
