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

@WebServlet("/documentbuildersafeexpand")
public class DocumentBuilderSafeExpandTestCase extends XXETestCase {

    /*
     * DocumentBuilder: "Safe" when Disabling Entity Expansion Example (FAILURE)
     * By setting the DocumentBuilderFactory's setExpandEntityReferences to false, it is supposed to ignore DTDs,
     * however, it does not
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setExpandEntityReferences(false);	// supposed to be safe but isn't!
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
