package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.SAXHandler;
import com.aspectsecurity.unittestsweb.XXETestCase;
import nu.xom.Builder;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xerces.jaxp.SAXParserImpl;
import org.xml.sax.XMLReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/xomunsafexmlreader")
public class XOMUnsafeXMLReaderTestCase extends XXETestCase {

    /*
     * XOM: Unsafe when Building from an Unsafe XMLReader Example
     * Proves that when building the XOM Document from an unsafe XMLReader, the Document parses the DTD
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // creating the parser
        SAXParserFactoryImpl saxParserFactory = new SAXParserFactoryImpl();
        SAXParserImpl saxParser;

        // parsing the XML
        try {
            saxParser = (SAXParserImpl) saxParserFactory.newSAXParser();
            SAXHandler handler = new SAXHandler();
            XMLReader xreader = saxParser.getXMLReader();
            xreader.setContentHandler(handler);

            Builder builder = new Builder(xreader);
            nu.xom.Document doc = builder.build(new ByteArrayInputStream(request.getParameter("payload").getBytes()));    // unsafe!

            // testing the result
            printResults(expectedSafe, doc.toXML(), response);

        } catch (Exception ex) {
            printResults(expectedSafe, ex, response);    // safe: exception thrown when parsing XML
        }
    }
}
