package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet("/transformersafedtd")
public class TransformerSafeDTDTestCase extends XXETestCase {

    /*
     * Transformer: Safe when Disallowing DTDs though a Safe XMLInputFactory Reader Example
     * Proves that disabling support for DTD in an XMLInputFactory and then transforming the XML through a safe
     * reader from the XMLInputFactory makes it throw an exception when it sees an external entity reference
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            XMLInputFactory inputFactory = XMLInputFactory.newFactory();
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);  // safe!

            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));
            StAXSource input = new StAXSource(streamReader);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult output = new StreamResult(baos);

            transformer.transform(input, output);
            String result = new String(baos.toByteArray());

            // testing the result
            printResults(expectedSafe, result, response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
