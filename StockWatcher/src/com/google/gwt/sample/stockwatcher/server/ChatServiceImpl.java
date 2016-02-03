package com.google.gwt.sample.stockwatcher.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.*;

/**
 * The server-side implementation of the HTTP service.
 */
@SuppressWarnings("serial")
public class ChatServiceImpl extends HttpServlet {
	int counter = 0;
	Connection conn = null;
	Object chat_id = null;
	Object employee = null;
	Object date = null;
	Object wait_time = null;
	Object url = null;
	Object transcript_alias = null;
	Object transcript_date = null;
	Object transcript_message = null;
	String query = null;
	ArrayList<Object> dataList = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// set content type header before accessing the Writer
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		onLoad();
		// then write the response
		out.println("<html>" + "<head><title> Message received  </title> </html>");

		out.close();
		// InputStream input = request.getInputStream();
		// System.out.println(input);
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = request.getReader();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} finally {
			reader.close();
		}

		String value = sb.toString();
		System.out.println(value);
		Map jsonJavaRootObject = new Gson().fromJson(value, Map.class);
		chat_id = jsonJavaRootObject.get("id");

		date = jsonJavaRootObject.get("created_at_date");
		url = jsonJavaRootObject.get("url");
		wait_time = jsonJavaRootObject.get("chat_waittime");

		ArrayList<JSONObject> transcripts = (ArrayList<JSONObject>) jsonJavaRootObject.get("transcripts");
		// int flag = 0;
		query = "select name from employees";
		PreparedStatement prepStatement;
		try {
			prepStatement = conn.prepareStatement(query);
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				dataList.add(rs.getString(1));
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (int i = 0; i < transcripts.size(); i++) {
			Map transcript_entry = (Map) transcripts.get(i);
			Object alias = transcript_entry.get("alias");
			for (int j = 0; j < dataList.size(); j++) {
				if (dataList.get(j).equals(alias)) {
					employee = alias;
				}
			}
		}

		try {
			query = "INSERT INTO chats (chat_id ,employee ,date ,wait_time ,url )" + "VALUES (?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, String.valueOf(chat_id));
			preparedStatement.setString(2, String.valueOf(employee));
			preparedStatement.setString(3, String.valueOf(date));
			preparedStatement.setDouble(4, (Double) wait_time);
			preparedStatement.setString(5, String.valueOf(url));
			preparedStatement.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < transcripts.size(); i++) {
			try {
				Map transcript_entry = (Map) transcripts.get(i);
				transcript_alias = transcript_entry.get("alias");
				transcript_date = transcript_entry.get("date");
				transcript_message = transcript_entry.get("message");
				query = "INSERT INTO chat_details(chat_id ,alias ,date ,message )" + "VALUES (?,?,?,?)";
				PreparedStatement preparedStatement2 = conn.prepareStatement(query);
				preparedStatement2.setString(1, String.valueOf(chat_id));
				preparedStatement2.setString(2, String.valueOf(transcript_alias));
				preparedStatement2.setString(3, String.valueOf(transcript_date));
				preparedStatement2.setString(4, String.valueOf(transcript_message));

				preparedStatement2.executeUpdate();

				// } catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void onLoad() {
		String url = null;
		Boolean val = null;

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

		try {
			counter++;
			String sql = "INSERT INTO counter_chat (counter)" + "VALUES (?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, String.valueOf(counter));
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error" + e);
			// e.printStackTrace();
		}
		System.out.println(val);

	}

}
