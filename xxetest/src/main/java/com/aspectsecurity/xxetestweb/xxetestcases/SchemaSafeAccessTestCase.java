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

@WebServlet("/schemasafeaccess")
public class SchemaSafeAccessTestCase extends XXETestCase {

    /*
     * Schema: Safe when Disallowing External DTDs and Schemas in Java 7u40 and up Example
     * Proves that setting SchemaFactory's ACCESS_EXTERNAL_DTD and ACCESS_EXTERNAL_SCHEMA properties makes the
     * Validator throw an exception when encountering a DTD (Java 7u40 and up only feature)
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");		// safe!
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");	// safe!
            Schema schema = factory.newSchema(getClass().getResource("/test.xsd"));
            Validator validator = schema.newValidator();

            StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult output = new StreamResult(baos);

            validator.validate(input, output);
            String result = new String(baos.toByteArray());

            // testing the result
            printResults(expectedSafe, result, response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
