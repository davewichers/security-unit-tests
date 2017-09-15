package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;
import org.jdom2.input.SAXBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/saxbuildersafeexpand")
public class SAXBuilderSafeExpandTestCase extends XXETestCase {

    /*
     * SAXBuilder: Safe when Disabling Entity Expansion Example
     * Proves that disabling entity expansion for the SAXBuilder makes it safe from injection
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setExpandEntities(false);
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
