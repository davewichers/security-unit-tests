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

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

@WebServlet("/documentbuildersafedoctype")
public class DocumentBuilderSafeDoctypeTestCase extends XXETestCase {
	
	static {
		BasicConfigurator.configure();
	}

	static final Logger logger = Logger.getLogger(DocumentBuilderSafeDoctypeTestCase.class);

    /*
     * DocumentBuilder: Safe when Disallowing DOCTYPE Declarations Example
     * Proves that disallowing DOCTYPE declarations for the DocumentBuilderFactory makes the DocumentBuilder
     * throw an exception when it sees a DTD
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);		// safe!
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            String payloadParam = request.getParameter("payload");
            logger.warn("Payload param value is: " + payloadParam);

            Document doc = docBuilder.parse(new ByteArrayInputStream(payloadParam.getBytes()));
            
            // testing the result
            printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
