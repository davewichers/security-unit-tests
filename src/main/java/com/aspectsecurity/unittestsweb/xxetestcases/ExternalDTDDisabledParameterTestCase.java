package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@WebServlet({"/externaldtddisabledxmlparameter", "/externaldtddisabled"})
public class ExternalDTDDisabledParameterTestCase extends XXETestCase {

    @Override
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            final XMLReader reader = factory.newSAXParser().getXMLReader();
            TestHandler testHandler = new TestHandler();
            reader.setContentHandler(testHandler);
            InputStream is = new ByteArrayInputStream(request.getParameter("payload").getBytes());
            reader.parse(new InputSource(is)); // if contrast protect is blocking XXE, exception is thrown here.
            // If testData were returned to the client, they would see secret/sensitive data.
            printResults(false,  testHandler.getTestData(), response);
        } catch (SAXException|SecurityException ex) {
            printResults(true, ex, response);	// safe: exception thrown when parsing XML
        } catch (ParserConfigurationException e) {
            throw new IOException(e);
        }
    }

}
