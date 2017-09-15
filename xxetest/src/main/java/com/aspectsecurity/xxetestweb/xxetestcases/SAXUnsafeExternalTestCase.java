package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetest.SAXHandler;
import com.aspectsecurity.xxetestweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/saxunsafeexternal")
public class SAXUnsafeExternalTestCase extends XXETestCase {

    /*
     * SAXParser: Unsafe when Enabling External General and Parameter Entities Example
     * Proves that enabling external general and parameter entities for the SAXParserFactory makes the SAXParser parse entities
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);		// unsafe!
            saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);	// unsafe!
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SAXHandler handler = new SAXHandler();

            saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);

            // testing the result
            printResults(expectedSafe, handler.getTestValue(), response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
