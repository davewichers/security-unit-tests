package com.aspectsecurity.xxetestweb.xquerytestcases;

import com.aspectsecurity.xxetestweb.XQueryTestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/stubxquery")
public class XQueryStubTestCase extends XQueryTestCase {

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
            printResults(expectedSafe, new ArrayList<String>(), response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
