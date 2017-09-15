package com.aspectsecurity.xxetestweb.xpathtestcases;

import com.aspectsecurity.xxetestweb.InvalidParameterException;
import com.aspectsecurity.xxetestweb.XPathTestCase;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/xalansafelist")
public class XalanSafeListTestCase extends XPathTestCase {

    /*
     * Xalan: Safe when Whitelisting on XPath Expression Example
     * Proves that Xalan is safe from injection when whitelisting on the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            //parsing the XML
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
            NodeList nodeList = XPathAPI.eval(document, query).nodelist();

            // testing the result
            printResults(expectedSafe, nodeList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
