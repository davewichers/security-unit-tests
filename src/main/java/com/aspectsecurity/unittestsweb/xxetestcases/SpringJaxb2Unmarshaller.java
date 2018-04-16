package com.aspectsecurity.unittestsweb.xxetestcases;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aspectsecurity.unittestsweb.ExternalTestCaseHelper;
import com.aspectsecurity.unittestsweb.XXETestCase;

@WebServlet("/springJaxb2Unmarshaller")
public class SpringJaxb2Unmarshaller extends XXETestCase {

    
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean expectedSafe = true;
	if (request.getParameter("unsafe") != null) {
		if (request.getParameter("unsafe").equalsIgnoreCase("true"))
			expectedSafe = false;
	}

	String path = "/springJaxb2Unmarshaller";
        String results = ExternalTestCaseHelper.sendExternalRequest(path, request);

	if (results != null)
	{
		printResults(expectedSafe, results, response);
	}
        
    }
}
