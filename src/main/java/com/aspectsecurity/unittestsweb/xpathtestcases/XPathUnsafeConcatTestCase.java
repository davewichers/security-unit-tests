package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.XPathTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

@WebServlet("/xpathunsafeconcat")
public class XPathUnsafeConcatTestCase extends XPathTestCase {

    /*
     * Java XPath: Unsafe when Using String Concatenation on XPathExpression Example
     * Proves that XPath is vulnerable to injection when using string concatenation on the XPathExpression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        try {
            // parsing the XML
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
            NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

            // testing the result
            printResults(expectedSafe, nodeList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
