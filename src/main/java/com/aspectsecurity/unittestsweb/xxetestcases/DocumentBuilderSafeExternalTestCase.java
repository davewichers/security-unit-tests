package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import org.w3c.dom.Document;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/documentbuildersafeexternal")
public class DocumentBuilderSafeExternalTestCase extends XXETestCase {

    /*
     * DocumentBuilder: Safe when Disabling External General Entities Example
     * Proves that disabling external general entities for the DocumentBuilderFactory makes the DocumentBuilder
     * ignore DTDs
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);	// safe!
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

            // testing the result
            printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
