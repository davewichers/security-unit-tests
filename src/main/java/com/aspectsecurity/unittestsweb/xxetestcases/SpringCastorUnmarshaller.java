package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.jaxb.BookType;
import com.aspectsecurity.unittests.jaxb.Collection;
import com.aspectsecurity.unittestsweb.XXETestCase;
import com.aspectsecurity.unittestsweb.RequestUrl;
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
