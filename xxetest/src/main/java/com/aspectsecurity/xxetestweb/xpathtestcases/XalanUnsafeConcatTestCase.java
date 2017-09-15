package com.aspectsecurity.xxetestweb.xpathtestcases;

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

@WebServlet("/xalanunsafeconcat")
public class XalanUnsafeConcatTestCase extends XPathTestCase {

    /*
     * Xalan: Unsafe when Using String Concatenation on XPath Expression Example
     * Proves that Xalan is vulnerable to injection when using string concatenation on the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        try {
            //parsing the XML
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

            // querying the XML
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
            NodeList nodeList = XPathAPI.eval(document, query).nodelist();

            // testing the result
            printResults(expectedSafe, nodeList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
