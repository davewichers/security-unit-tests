package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.jaxb.BookType;
import com.aspectsecurity.unittests.jaxb.Collection;
import com.aspectsecurity.unittestsweb.XXETestCase;
import com.aspectsecurity.unittestsweb.RequestUrl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

@WebServlet("/springCastorUnmarshaller")
public class SpringCastorUnmarshaller extends XXETestCase {

    
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean expectedSafe = true;
	// TODO better exception handling
	String url = "http://localhost:9000/springCastorUnmarshaller";	// TODO this is bad... should come from a configurable property
	String payload = request.getParameter("payload");

	if (request.getParameter("unsafe") != null)
	{
		if (request.getParameter("unsafe").equalsIgnoreCase("true"))
		{
			expectedSafe = false;
		}
	}
	try {
		url += "?person="+URLEncoder.encode(payload, "UTF-8");
		url += "&unsafe="+URLEncoder.encode(Boolean.toString(!expectedSafe), "UTF-8");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	HashMap<String, String> headers = new HashMap<String, String>();
	String results = RequestUrl.executeGet(url, headers);
	if (results != null)
	{
		printResults(expectedSafe, results, response);
	}
        
    }
}
