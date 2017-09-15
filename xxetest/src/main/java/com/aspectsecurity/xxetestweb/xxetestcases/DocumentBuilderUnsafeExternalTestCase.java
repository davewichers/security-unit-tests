package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;
import org.w3c.dom.Document;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/documentbuilderunsafeexternal")
public class DocumentBuilderUnsafeExternalTestCase extends XXETestCase {

    /*
     * DocumentBuilder: Unsafe when Enabling External General Entities Example
     * Proves that enabling external general entities for the DocumentBuilderFactory leaves the DocumentBuilder
     * unsafe from malicious entities
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);	// unsafe!
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
