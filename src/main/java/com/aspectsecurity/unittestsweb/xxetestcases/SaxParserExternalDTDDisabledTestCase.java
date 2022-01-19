package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import org.xml.sax.InputSource;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/saxparserexternaldtddisabled")
public class SaxParserExternalDTDDisabledTestCase extends XXETestCase {
    @Override
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final boolean expectedSafe = false;
        try {
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            final SAXParser parser = factory.newSAXParser();
            TestHandler testHandler = new TestHandler();
            InputStream is = new ByteArrayInputStream(request.getParameter("payload").getBytes());
            parser.parse(new InputSource(is), testHandler); // if contrast protect is blocking XXE, exception is thrown here.
            // If testData were returned to the client, they would see secret/sensitive data.
            printResults(expectedSafe,  testHandler.getTestData(), response);	// safe: exception thrown when parsing XML
        } catch (Exception ex) {
            printResults(true, ex, response);	// safe: exception thrown when parsing XML
        }
    }

}
