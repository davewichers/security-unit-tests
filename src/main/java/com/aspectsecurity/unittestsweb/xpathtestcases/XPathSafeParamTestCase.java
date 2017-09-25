package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.XPathTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;
import java.io.IOException;

@WebServlet("/xpathsafeparam")
public class XPathSafeParamTestCase extends XPathTestCase {

    /*
     * Java XPath: Safe when Parameterizing on XPathExpression Example
     * Proves that XPath is safe from injection when using an XPathVariableResolver to parameterize the XPathExpression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // parsing the XML
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            final HttpServletRequest finalRequest = request;    // for Java compiler 1.7
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setXPathVariableResolver(new XPathVariableResolver() {
                @Override
                public Object resolveVariable(QName variableName) {
                    if (variableName.getLocalPart().equals("name")) {
                        return finalRequest.getParameter("payload");
                    }
                    else {
                        return null;
                    }
                }
            });
            String query = "/Students/Student[FirstName/text()=$name]"; // safe!
            NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

            // testing the result
            printResults(expectedSafe, nodeList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
