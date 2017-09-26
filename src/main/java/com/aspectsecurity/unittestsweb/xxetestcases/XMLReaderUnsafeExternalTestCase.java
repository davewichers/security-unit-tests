package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.SAXHandler;
import com.aspectsecurity.unittestsweb.XXETestCase;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/xmlreaderunsafeexternal")
public class XMLReaderUnsafeExternalTestCase extends XXETestCase {

    /*
     * XMLReader: Unsafe when Enabling External General and Parameter Entities Example
     * Proves that enabling external general and parameter entities for the XMLReader makes it parse entities
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            SAXHandler handler = new SAXHandler();
            XMLReader reader = saxParser.getXMLReader();
            reader.setFeature("http://xml.org/sax/features/external-general-entities", true);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
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
