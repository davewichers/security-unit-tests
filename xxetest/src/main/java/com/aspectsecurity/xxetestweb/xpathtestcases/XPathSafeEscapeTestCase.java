package com.aspectsecurity.xxetestweb.xpathtestcases;

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

@WebServlet("/xpathsafeescape")
public class XPathSafeEscapeTestCase extends XPathTestCase {

    /*
     * Java XPath: Safe when Escaping Apostrophes on XPathExpression Example
     * Proves that XPath is safe from injection when using string concatenation while escaping apostrophes on
     * the XPathExpression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // parsing the XML
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            XPath xPath = XPathFactory.newInstance().newXPath();
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
            NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

            // testing the result
            printResults(expectedSafe, nodeList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
