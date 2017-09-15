package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet("/schemaunsafe")
public class SchemaUnsafeTestCase extends XXETestCase {

    /*
     * Schema: Unsafe by Default Example
     * Proves that SchemaFactory parses entities by default
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(getClass().getResource("/test.xsd"));
            Validator validator = schema.newValidator();

            StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult output = new StreamResult(baos);

            validator.validate(input, output);	// unsafe!
            String result = new String(baos.toByteArray());

            // testing the result
            printResults(expectedSafe, result, response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
