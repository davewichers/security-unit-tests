package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import org.jdom2.input.SAXBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/saxbuilderunsafeexpand")
public class SAXBuilderUnsafeExpandTestCase extends XXETestCase {

    /*
     * SAXBuilder: Unsafe when Enabling Entity Expansion Example
     * Proves that enabling entity expansion for the SAXBuilder makes it unsafe from injection
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setExpandEntities(true);
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
