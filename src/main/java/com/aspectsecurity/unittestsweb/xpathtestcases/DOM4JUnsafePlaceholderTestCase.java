package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.XPathTestCase;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/dom4junsafeplaceholder")
public class DOM4JUnsafePlaceholderTestCase extends XPathTestCase {

    /*
     * DOM4J: Unsafe when Using String Placeholders on XPath Expression Example
     * Proves that DOM4J is vulnerable to injection when using string placeholders on the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        try {
            // parsing the XML
            org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
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
