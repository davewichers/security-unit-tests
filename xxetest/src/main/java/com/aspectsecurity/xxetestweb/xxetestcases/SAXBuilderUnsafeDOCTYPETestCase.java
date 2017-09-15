package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;
import org.jdom2.input.SAXBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/saxbuilderunsafedoctype")
public class SAXBuilderUnsafeDOCTYPETestCase extends XXETestCase {

    /*
     * SAXBuilder: Unsafe when Allowing DOCTYPE Declarations Example
     * Proves that allowing DOCTYPE declarations for the SAXBuilder allows it to parse entities
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl",false);
            org.jdom2.Document document = builder.build(new ByteArrayInputStream(request.getParameter("payload").getBytes()));
            org.jdom2.Element root = document.getRootElement();

            // testing the result
            printResults(expectedSafe, root.getText(), response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
