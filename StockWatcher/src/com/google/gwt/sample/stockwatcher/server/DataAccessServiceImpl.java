package com.google.gwt.sample.stockwatcher.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.sample.stockwatcher.client.DataAccessService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DataAccessServiceImpl extends RemoteServiceServlet implements DataAccessService {

	String url = null;
	Connection conn = null;
	String query = null;
	private static final Logger log = Logger.getLogger(ChatServiceImpl.class.getName());

	public String getChatsByEmployee(String input) throws IllegalArgumentException {

		startDBConnection();
		log.info("Inside sql service");
		String employee_name = input;
		log.info(input);
		startDBConnection();
		int count = 0;
		query = "select label,chat_id,date,wait_time from chats where employee= ?";
		StringBuilder sb = new StringBuilder();
		try {
			PreparedStatement prepStatement = conn.prepareStatement(query);
			prepStatement.setString(1, String.valueOf(employee_name));
			ResultSet rs = prepStatement.executeQuery();

			while (rs.next()) {
				count++;
				for (int i = 1; i < 5; i++) {
					sb.append(rs.getString(i));
					sb.append(';');
				}
				sb.append("\n");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info(String.valueOf(count));
		if (count == 0) {
			return "x";
		} else {
			return sb.toString();
		}

	}

	public String getChatsByDate(String input) throws IllegalArgumentException {

		startDBConnection();

		String date = input;
		query = "select chat_id,employee,wait_time from chats where date= ?";
		StringBuilder sb = new StringBuilder();
		try {
			PreparedStatement prepStatement = conn.prepareStatement(query);
			prepStatement.setString(1, String.valueOf(date));
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				for (int i = 1; i < 4; i++) {
					sb.append(rs.getString(1));
					sb.append(':');
				}
				sb.append("\n");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();

	}

	public String getChatDetails(String input) {
		startDBConnection();
		String label = input;

		query = "select chat_id,employee,wait_time,url from chats where label= ?";
		StringBuilder sb = new StringBuilder();
		try {
			PreparedStatement prepStatement = conn.prepareStatement(query);
			prepStatement.setString(1, String.valueOf(label));
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				for (int i = 1; i < 5; i++) {
					sb.append(rs.getString(i));
					sb.append(';');
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getChatData(String input) {
		startDBConnection();
		String label = input;

		query = "select date,alias,message from chat_details where chat_id= (select chat_id from chats where label = ?)";
		StringBuilder sb = new StringBuilder();
		try {
			PreparedStatement prepStatement = conn.prepareStatement(query);
			prepStatement.setString(1, String.valueOf(label));
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				for (int i = 1; i < 4; i++) {
					sb.append(rs.getString(i));
					sb.append(':');
				}
				sb.append("\n");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getStatistics() {
		startDBConnection();
		query = "SELECT AVG(wait_time) from chats";
		StringBuilder sb = new StringBuilder();
		try {
			PreparedStatement prepStatement = conn.prepareStatement(query);
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				sb.append(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getEmployees() {
		startDBConnection();
		query = "select * from employees";
		PreparedStatement prepStatement;
		try {
			prepStatement = conn.prepareStatement(query);
			ResultSet rs = prepStatement.executeQuery();
			StringBuilder sb = new StringBuilder();
			while (rs.next()) {

				sb.append(rs.getString(1));
				sb.append('\n');

			}
			return sb.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void startDBConnection() {

		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
			// Connecting from App Engine.
			// Load the class that provides the "jdbc:google:mysql://"
			// prefix.
			try {

				Class.forName("com.mysql.jdbc.GoogleDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			url = "jdbc:google:mysql://ashishchatstats:test/test_database";
			try {
				conn = DriverManager.getConnection(url, "root", "");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			url = "jdbc:mysql://173.194.251.17/test_database";
			try {
				conn = DriverManager.getConnection(url, "ashish", "Manutd19");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
