package com.aspectsecurity.xxetestweb.xpathtestcases;

import com.aspectsecurity.xxetestweb.XPathTestCase;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/stubxpath")
public class XPathStubTestCase extends XPathTestCase {

    /*
     * Stub: Test Case Stub
     * A template for adding test cases
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        try {
            // parsing the XML
            // querying the XML

            // testing the result
            printResults(expectedSafe, (NodeList) new Object(), response);
            printResults(expectedSafe, new ArrayList<String>(), response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
