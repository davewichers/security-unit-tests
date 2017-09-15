package com.aspectsecurity.xxetestweb;

import org.owasp.encoder.Encode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class XXETestCase extends TestCase {

    /**
     * {@inheritDoc}
     */
    protected abstract void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Prints the results of the test case
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param xmlContent	the contents of the XML after parsing
     * @param response		the servlet response
     * @throws IOException  in case the response has an error
     */
    protected void printResults(boolean expectedSafe, String xmlContent, HttpServletResponse response) throws IOException {

        response.getWriter().write("<html><head><title>Results</title></head><body>");

        response.getWriter().write("<h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />" + "Actual Result: ");

        if (xmlContent.contains("SUCCESSFUL") || xmlContent.startsWith("SUCC")) {
            response.getWriter().write("Unsafe! XXE was injected! :( </h3><br />" +
                    "<b>" + " XML Content (Should contain \"SUCCESSFUL\" or your custom XML entity):" +
                    "</b><br />" + Encode.forHtmlContent(xmlContent));
        }
        else {
            response.getWriter().write("XML Parser is safe! :) </h3><br />");
            if (xmlContent.equals("") || xmlContent.isEmpty()) {
                response.getWriter().write("<b>" + "XML Content:" + "</b>" + "<br />" + "The XML file is blank" + "<br /><br />");
            }
            else if (xmlContent.startsWith("XML was not parsed")) {
                response.getWriter().write("<b>" + "XML Content:" + "</b>" + "<br />" + xmlContent);
            }
            else {
                response.getWriter().write("<b>" + "XML Content:" + "</b>" + "<br />" + Encode.forHtmlContent(xmlContent));
            }
        }

        response.getWriter().write("<br /><br /><br /><a href=\"index.jsp\">&lt&lt&lt back to tests</a>");
        response.getWriter().write("</body></html>");
    }

    /**
     * Prints the results of the test case if there is an exception
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param ex			the exception thrown
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    protected void printResults(boolean expectedSafe, Exception ex, HttpServletResponse response) throws IOException {
        printResults(expectedSafe, "XML was not parsed due to a thrown exception" + "<b><br />" +"Stack Trace: " + "</b><br />" + ex.toString(), response);
    }
}
