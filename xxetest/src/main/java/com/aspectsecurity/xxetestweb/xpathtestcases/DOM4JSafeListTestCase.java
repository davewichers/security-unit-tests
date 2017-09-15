package com.aspectsecurity.xxetestweb.xpathtestcases;

import com.aspectsecurity.xxetestweb.InvalidParameterException;
import com.aspectsecurity.xxetestweb.XPathTestCase;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/dom4jsafelist")
public class DOM4JSafeListTestCase extends XPathTestCase {

    /*
     * DOM4J: Safe when Whitelisting on XPath Expression Example
     * Proves that DOM4J is safe from injection when whitelisting on the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // parsing the XML
            org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            String query;
            if (request.getParameter("payload").contains("'")) {
                printResults(expectedSafe, new ArrayList<String>(), response);
                throw new InvalidParameterException("First Name parameter must not contain apostrophes");
            }
            else {
                query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
            }
            ArrayList<String> nodeList = new ArrayList<String>();
            for (Node node : document.selectNodes(query)) {
                nodeList.add(node.toString() + "\n" + node.getStringValue());
            }

            // testing the result
            printResults(expectedSafe, nodeList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
