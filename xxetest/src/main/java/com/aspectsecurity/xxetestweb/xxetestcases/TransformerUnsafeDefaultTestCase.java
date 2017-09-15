package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet("/transformerunsafedefault")
public class TransformerUnsafeDefaultTestCase extends XXETestCase {

    /*
     * Transformer: Unsafe by Default Example
     * Proves that TransformerFactory parses entities by default
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult output = new StreamResult(baos);

            transformer.transform(input, output);	// unsafe!
            String result = new String(baos.toByteArray());

            // testing the result
            printResults(expectedSafe, result, response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
