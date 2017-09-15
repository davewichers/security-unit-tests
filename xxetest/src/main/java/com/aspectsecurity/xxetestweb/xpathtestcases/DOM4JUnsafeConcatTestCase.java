package com.aspectsecurity.xxetestweb.xpathtestcases;

import com.aspectsecurity.xxetestweb.XPathTestCase;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/dom4junsafeconcat")
public class DOM4JUnsafeConcatTestCase extends XPathTestCase {

    /*
     * DOM4J: Unsafe when Using String Concatenation on XPath Expression Example
     * Proves that DOM4J is vulnerable to injection when using string concatenation on the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        try {
            // parsing the XML
            org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
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
