package com.aspectsecurity.unittestsweb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// helper class
public class RequestUrl {
	
    private static final List<String> ALLOWED_METHODS = Collections.unmodifiableList(
	new ArrayList<String>() {{
	    add("GET");
	   // add("POST");
        }}
    );

    private static String executeRequest(String targetURL, String urlParameters, String method, HashMap<String, String> headers) {
	    // had some help from https://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java
	    HttpURLConnection connection = null;
	    
	    if (!ALLOWED_METHODS.contains(method))
	    {
		    return null;
	    }

	    try {
		    // create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod(method);

		    // set headers
		    for (String header : headers.keySet()) {
			    connection.setRequestProperty(header, headers.get(header));
		    }

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    // Send request
		    // post hasn't been tested yet
		    if (method.equals("POST") && urlParameters != null)
		    {
			    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			    wr.writeBytes(urlParameters);
			    wr.close();
		    }

		    // Get Response
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
			    response.append(line);
			    response.append("\r\n");
		    }
		    rd.close();
		    return response.toString();
	    } catch (Exception e) {
		    return "Server did not handle the request.";
	    } finally {
		    if (connection != null) {
			    connection.disconnect();
		    }
	    }
    }
    
    public static String executeGet(String targetURL, HashMap<String, String> headers) {
	    return executeRequest(targetURL, null, "GET", headers);
    }

}

