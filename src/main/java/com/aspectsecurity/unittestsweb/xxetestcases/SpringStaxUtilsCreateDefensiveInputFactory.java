package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.jaxb.BookType;
import com.aspectsecurity.unittests.jaxb.Collection;
import com.aspectsecurity.unittestsweb.XXETestCase;
import com.aspectsecurity.unittestsweb.ExternalTestCaseHelper;

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

@WebServlet("/springStaxUtilsCreateDefensiveInputFactory")
public class SpringStaxUtilsCreateDefensiveInputFactory extends XXETestCase {

    /*
     * JAXBContext: Unsafe (Safe in Java 1.8 and up) JAXBContext Unmarshaller from File Example
     * Proves that XML deserialized using a JAXB Unmarshaller is unsafe from malicious entities when
     * unmarshalled directly from File (Safe in 1.8 and up)
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean expectedSafe = true;
	String path = "/springStaxUtilsCreateDefensiveInputFactory";
	String results = ExternalTestCaseHelper.sendExternalRequest(path, request);
	if (results != null)
	{
		printResults(expectedSafe, results, response);
	}
        
    }
}
