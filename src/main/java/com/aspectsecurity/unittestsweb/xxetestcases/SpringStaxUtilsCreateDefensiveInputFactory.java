package com.aspectsecurity.unittestsweb.xxetestcases;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aspectsecurity.unittestsweb.ExternalTestCaseHelper;
import com.aspectsecurity.unittestsweb.XXETestCase;

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
