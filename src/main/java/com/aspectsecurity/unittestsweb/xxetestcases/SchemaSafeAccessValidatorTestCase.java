package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet("/schemasafeaccessvalidator")
public class SchemaSafeAccessValidatorTestCase extends XXETestCase {

    /*
     * Schema: Safe when Disallowing External DTDs and Schemas on the Validator in Java 7u40 and up Example
     * Proves that setting Validator's ACCESS_EXTERNAL_DTD and ACCESS_EXTERNAL_SCHEMA properties makes the
     * Validator throw an exception when encountering a DTD (Java 7u40 and up only feature)
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(getClass().getResource("/test.xsd"));
            javax.xml.validation.Validator validator = schema.newValidator();
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");	// safe!
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");	// safe!

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
