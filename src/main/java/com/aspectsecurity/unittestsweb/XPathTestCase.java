package com.aspectsecurity.unittestsweb;

import org.owasp.encoder.Encode;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public abstract class XPathTestCase extends TestCase {

    /**
     * {@inheritDoc}
     */
    protected abstract void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Prints the results of the XPath test case that uses NodeList
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param nodeList	    the result of the XPath query
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    protected void printResults(boolean expectedSafe, NodeList nodeList, HttpServletResponse response) throws IOException {

        boolean actuallySafe = true;
        if (nodeList.getLength() > 1) {
            actuallySafe = false;
        }

        response.getWriter().write("<html><head><title>Results</title></head><body>");

        response.getWriter().write("<h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XPath query is safe! :)" : "Unsafe XPath query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student or empty result" : "Should be all Students") + "):</b><br /><pre>");

        // print all Node information
        for (int i = 0; i < nodeList.getLength(); i++) {
            response.getWriter().write("Student " + nodeList.item(i).getAttributes().getNamedItem("studentId").getTextContent() + ":<br />");
            for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                switch (j) {
                    case 1: response.getWriter().write("\tLast Name:\t");   break;
                    case 3: response.getWriter().write("\tFirst Name:\t");  break;
                    case 5: response.getWriter().write("\tUsername:\t");    break;
                    case 7: response.getWriter().write("\tPassword:\t");    break;
                }
                if (!((j % 2) == 0)) {
                    response.getWriter().write(nodeList.item(i).getChildNodes().item(j).getTextContent() + "<br />");
                }
            }
        }

        response.getWriter().write("</pre>");
        response.getWriter().write("<br /><br /><br /><a href=\"xpath.jsp\">&lt&lt&lt back to tests</a><br /><br />");
        response.getWriter().write("</body></html>");
    }

    /**
     * Prints the results of the XPath test case
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param resultList	the result of the XPath query
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
        response.getWriter().write("Actual result: " + (actuallySafe ? "XPath query is safe! :)" : "Unsafe XPath query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student or empty result" : "Should be all Students") + "):</b><br /><pre>");

        for (String student : resultList) {
            response.getWriter().write(Encode.forHtmlContent(student) + "<br />");
        }

        response.getWriter().write("</pre>");
        response.getWriter().write("<br /><br /><br /><a href=\"xpath.jsp\">&lt&lt&lt back to tests</a><br /><br />");
        response.getWriter().write("</body></html>");
    }
}
