package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import org.jdom2.input.SAXBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/saxbuilderunsafeexternalon")
public class SAXBuilderUnsafeExternalOnTestCase extends XXETestCase {

    /*
     * SAXBuilder: Unsafe when Enabling External General and Parameter Entities Example
     * Proves that enabling external general and parameter entities for the SAXBuilder leaves it vulnerable to
     * malicious entities
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setFeature("http://xml.org/sax/features/external-general-entities", true);
            builder.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
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
