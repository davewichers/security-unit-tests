package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import org.codehaus.stax2.XMLInputFactory2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/xmlinput2safeexternal")
public class XMLInput2SafeExternalTestCase extends XXETestCase {

    /*
     * XMLInputFactory2: Safe when Disallowing External Entities Example
     * Proves that setting XMLInputFactory2's IS_SUPPORTING_EXTERNAL_ENTITIES attribute to false makes the
     * XMLStreamReader ignore external entity references
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory2.newInstance();
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

            String result = "";
            while(xmlStreamReader.hasNext()) {
                xmlStreamReader.next();
                if (xmlStreamReader.isStartElement()) {
                    if (xmlStreamReader.getLocalName().equals("test")) {
                        result = xmlStreamReader.getElementText();
                    }
                }
            }

            // testing the result
            printResults(expectedSafe, result, response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
