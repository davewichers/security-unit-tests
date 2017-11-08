package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.SAXHandler;
import com.aspectsecurity.unittestsweb.XXETestCase;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/xmlreaderfactoryunsafe")
public class XMLReaderFactoryUnsafeTestCase extends XXETestCase {

    /*
     * XMLReaderFactory: Unsafe by default Example
     * Proves that the default XMLReaderFactory is unsafe by default.
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
        	// NOTE: XMLReaderFactory is a static class that can only be used to create XMLReader objects.
        	// There is no API for this class to configure it to construct safe XMLReader objects.
        	// Your only option to make the XMLReader safe is to set the right features on the XMLReader
        	// itself. Those options are covered by the XMLReader specific test cases.
            XMLReader reader = XMLReaderFactory.createXMLReader();
            SAXHandler handler = new SAXHandler();
            reader.setContentHandler(handler);

            reader.parse(new InputSource(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

            // testing the result
            printResults(expectedSafe, handler.getTestValue(), response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
