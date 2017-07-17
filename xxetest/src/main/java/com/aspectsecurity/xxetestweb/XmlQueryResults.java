package com.aspectsecurity.xxetestweb;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
import java.util.ArrayList;

/**
 * Servlet implementation class XmlQueryResults
 */
@WebServlet("/xmlqueryresults")
public class XmlQueryResults extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmlQueryResults() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader("X-Frame-Options", "DENY");    // Anti-Clickjacking Controls

        /*
         * Detects which test case we're running, runs it, and prints the results
         */
        switch (request.getParameter("var")) {

            //region Stub: Test Case Stub
            /*
             * This is a template for adding test cases
             */
            case "stub": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    // querying the XML

                    // testing the result
                    printResults(expectedSafe, (NodeList) new Object(), response);
                    //printResults(expectedSafe, new ArrayList<String>(), request, response);
                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region Java XPath: Unsafe when Using String Concatenation on XPathExpression Example
            /*
             * Proves that XPath is vulnerable to injection when using string concatenation on the XPathExpression
             */
            case "xpathunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
                    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region Java XPath: Unsafe when Using String Placeholders on XPathExpression Example
            /*
             * Proves that XPath is vulnerable to injection when using string placeholders on the XPathExpression
             */
            case "xpathunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
                    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region Java XPath: Safe when Parameterizing on XPathExpression Example
            /*
             * Proves that XPath is safe from injection when using an XPathVariableResolver to parameterize the XPathExpression
             */
            case "xpathsafeparam": {
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
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region Java XPath: Safe when Escaping Apostrophes on XPathExpression Example
            /*
             * Proves that XPath is safe from injection when using string concatenation while escaping apostrophes on
             * the XPathExpression
             */
            case "xpathsafeescape": {
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
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region Xalan: Unsafe when Using String Concatenation on XPath Expression Example
            /*
             * Proves that Xalan is vulnerable to injection when using string concatenation on the XPath expression
             */
            case "xalanunsafeconcat": {
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
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region Xalan: Unsafe when Using String Placeholders on XPath Expression Example
            /*
             * Proves that Xalan is vulnerable to injection when using string placeholders on the XPath expression
             */
            case "xalanunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    //parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
                    NodeList nodeList = XPathAPI.eval(document, query).nodelist();

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region Xalan: Safe when Escaping Apostrophes on XPathExpression Example
            /*
             * Proves that XPath is safe from injection when using string concatenation while escaping apostrophes on
             * the XPath expression
             */
            case "xalansafeescape": {
                final boolean expectedSafe = true;

                try {
                    //parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
                    NodeList nodeList = XPathAPI.eval(document, query).nodelist();

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            default:
                response.getWriter().write("<html><head><title>Error</title></head><body>");
                response.getWriter().write("Error: Test case not found for \"" + request.getParameter("var") + "\"");
                response.getWriter().write("</body></html>");
                break;
        }
    }

    /**
     * Prints the results of the XPath test case
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param nodeList	    the result of the XPath query
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    private void printResults(boolean expectedSafe, NodeList nodeList, HttpServletResponse response) throws IOException {

        boolean actuallySafe = true;
        if (nodeList.getLength() > 1) {
            actuallySafe = false;
        }

        response.getWriter().write("<html><head><title>Results</title></head><body><h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XPath is safe! :)" : "Unsafe query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should be empty result" : "Should be all Students") + "):</b><br /><pre>");

        // print all Node information
        for (int i = 0; i < nodeList.getLength(); i++) {
            response.getWriter().write("Student " + nodeList.item(i).getAttributes().getNamedItem("studentId").getTextContent() + ":<br />");
            for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                switch (j) {
                    case 1: response.getWriter().write("\tLast Name:\t");   break;
                    case 3: response.getWriter().write("\tFirst Name:\t");  break;
                    case 5: response.getWriter().write("\tUsername:\t");    break;
                    case 7: response.getWriter().write("\tPassword:\t");    break;
                }
                if (!((j % 2) == 0)) {
                    response.getWriter().write(nodeList.item(i).getChildNodes().item(j).getTextContent() + "<br />");
                }
            }
        }

        response.getWriter().write("</pre><br /><br /><br /><a href=\"xmlquery.jsp\">&lt&lt&lt back to tests</a>");
        response.getWriter().write("</body></html>");
    }

    /**
     * Prints the results of the XQuery test case
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param resultList	the result of the XQuery query
     * @param request       the servlet request
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    private void printResults(boolean expectedSafe, ArrayList<String> resultList, HttpServletRequest request, HttpServletResponse response) throws IOException {

        /*boolean actuallySafe = true;
        if (nodeList.getLength() > 1) {
            actuallySafe = false;
        }

        response.getWriter().write("<html><head><title>Results</title></head><body><h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XPath is safe! :)" : "Unsafe query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student" : "Should be all Students") + "):</b><br /><pre>");

        for (int i = 0; i < resultList.getLength(); i++) {
            response.getWriter().write("Student " + nodeList.item(i).getAttributes().getNamedItem("studentId").getTextContent() + ":<br />");
            for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                switch (j) {
                    case 1: response.getWriter().write("\tLast Name:\t");   break;
                    case 3: response.getWriter().write("\tFirst Name:\t");  break;
                    case 5: response.getWriter().write("\tUsername:\t");    break;
                    case 7: response.getWriter().write("\tPassword:\t");    break;
                }
                if (!((j % 2) == 0)) {
                    response.getWriter().write(nodeList.item(i).getChildNodes().item(j).getTextContent() + "<br />");
                }
            }
        }

        response.getWriter().write("</pre><br /><br /><br /><a href=\"xmlquery.jsp\">&lt&lt&lt back to tests</a>");
        response.getWriter().write("</body></html>");*/
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
