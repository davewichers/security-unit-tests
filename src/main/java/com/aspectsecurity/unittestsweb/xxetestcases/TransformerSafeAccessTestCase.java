package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.App;
import com.aspectsecurity.unittestsweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet("/transformersafeaccess")
public class TransformerSafeAccessTestCase extends XXETestCase {

    /*
     * Transformer: Safe when Disallowing External DTDs in Java 7u40 and up Example
     * Proves that setting TransformerFactory's ACCESS_EXTERNAL_DTD attribute to null makes the Transformer
     * throw an exception when it sees an external entity reference (Java 7u40 and up only feature)
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
System.out.println(App.getJaxpImplementationInfo("TransformerFactory", factory.getClass()));
System.out.println(App.getJaxpImplementationInfo("Transformer", factory.newTransformer().getClass()));
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");	// safe!
            Transformer transformer = factory.newTransformer();

            StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult output = new StreamResult(baos);

            transformer.transform(input, output);
            String result = new String(baos.toByteArray());

            // testing the result
            printResults(expectedSafe, result, response);
        }
        catch (Exception ex) {
System.out.println("TransformerSafeAccessTestCase: caught exception:");
ex.printStackTrace();
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
