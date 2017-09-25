package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.XPathTestCase;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/dom4jsafeescape")
public class DOM4JSafeEscapeTestCase extends XPathTestCase {

    /*
     * DOM4J: Safe when Escaping Apostrophes on XPath Expression Example
     * Proves that DOM4J is safe from injection when using string concatenation while escaping apostrophes on
     * the XPathExpression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // parsing the XML
            org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
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
