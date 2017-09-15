package com.aspectsecurity.xxetestweb.xpathtestcases;

import com.aspectsecurity.xxetestweb.InvalidParameterException;
import com.aspectsecurity.xxetestweb.XPathTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/xpathsafelist")
public class XPathSafeListTestCase extends XPathTestCase {

    /*
     * Java XPath: Safe when Whitelisting on XPathExpression Example
     * Proves that XPath is safe from injection when whitelisting on the XPathExpression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // parsing the XML
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            String query;
            if (request.getParameter("payload").contains("'")) {
                printResults(expectedSafe, new ArrayList<String>(), response);
                throw new InvalidParameterException("First Name parameter must not contain apostrophes");
            }
            else {
                query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
            }
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

            // testing the result
            printResults(expectedSafe, nodeList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
