package com.aspectsecurity.unittestsweb.xxetestcases;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aspectsecurity.unittestsweb.ExternalTestCaseHelper;
import com.aspectsecurity.unittestsweb.XXETestCase;

@WebServlet("/springCastorUnmarshaller")
public class SpringCastorUnmarshaller extends XXETestCase {

    
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean expectedSafe = true;

        if (request.getParameter("unsafe") != null) {
                if (request.getParameter("unsafe").equalsIgnoreCase("true"))
                        expectedSafe = false;
        }

        String path = "/springCastorUnmarshaller";
        String results = ExternalTestCaseHelper.sendExternalRequest(path, request);
	if (results != null)
	{
		printResults(expectedSafe, results, response);
	}
        
    }
}
