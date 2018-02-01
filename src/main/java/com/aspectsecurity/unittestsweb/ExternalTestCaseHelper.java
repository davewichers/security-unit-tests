package com.aspectsecurity.unittestsweb;

import org.owasp.encoder.Encode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

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
