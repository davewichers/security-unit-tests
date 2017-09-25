package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import org.codehaus.stax2.XMLInputFactory2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/xmlinput2safeaccess")
public class XMLInput2SafeAccessTestCase extends XXETestCase {

    /*
     * XMLInputFactory2: Safe when Disallowing External DTDs in Java 7u40 and up Example
     * Proves that setting XMLInputFactory2's ACCESS_EXTERNAL_DTD attribute to null makes the XMLStreamReader
     * throw an exception when it sees an external entity reference (Java 7u40 and up only feature)
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory2.newInstance();
            xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");

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
