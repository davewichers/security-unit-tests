package com.aspectsecurity.unittestsweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class ExternalTestCaseHelper {

	public static String sendExternalRequest (String path, HttpServletRequest request) {
		// TODO better exception handling
		PropertiesHelper props = PropertiesHelper.getInstance();
		String address = props.getProperty("server.address");
		String port = props.getProperty("server.port");
		String proto = props.getProperty("server.proto");
		String url = proto + "://" + address + ":" + port + path; // path includes leading slash

		String params = request.getParameter("payload");

		try {
			url += "?person="+URLEncoder.encode(params, "UTF-8");
			
			if (request.getParameter("unsafe") != null) {
				String unsafe = "false";
				if (request.getParameter("unsafe").equalsIgnoreCase("true"))
					unsafe = "true";
			
				url += "&unsafe="+URLEncoder.encode(unsafe, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HashMap<String, String> headers = new HashMap<String, String>();
		return RequestUrl.executeGet(url, headers);
	} 

    }
