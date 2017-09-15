package com.aspectsecurity.xxetestweb;

import org.owasp.encoder.Encode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public abstract class XQueryTestCase extends TestCase {

    /**
     * {@inheritDoc}
     */
    protected abstract void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Prints the results of the XQuery test case
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param resultList	the result of the XQuery query
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    protected void printResults(boolean expectedSafe, ArrayList<String> resultList, HttpServletResponse response) throws IOException {

        boolean actuallySafe = true;
        if (resultList.size() > 1) {
            actuallySafe = false;
        }

        response.getWriter().write("<html><head><title>Results</title></head><body>");

        response.getWriter().write("<h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XQuery query is safe! :)" : "Unsafe XQuery query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student, a thrown exception, or an empty result" : "Should be all Students") + "):</b><br /><pre>");

        for (String student : resultList) {
            response.getWriter().write(Encode.forHtmlContent(student) + "<br />");
        }

        response.getWriter().write("</pre>");
        response.getWriter().write("<br /><br /><br /><a href=\"xquery.jsp\">&lt&lt&lt back to tests</a><br /><br />");
        response.getWriter().write("</body></html>");
    }
}
