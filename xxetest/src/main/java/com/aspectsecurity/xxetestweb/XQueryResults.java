package com.aspectsecurity.xxetestweb;

import com.saxonica.xqj.SaxonXQDataSource;
import org.owasp.encoder.Encode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.xquery.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet implementation class XQueryResults
 */
@WebServlet("/xqueryresults")
public class XQueryResults extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public XQueryResults() {
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
                    printResults(expectedSafe, new ArrayList<>(), response);

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            /* ********************************************************************************************************
             * Using com.saxonica.xqj.SaxonXQDataSource (Saxonica Saxon9)
             * ********************************************************************************************************/
            //region XQJ with Saxon: Unsafe when Using String Concatenation on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses string concatenation
             * makes it vulnerable to injection
             */
            case "saxonunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                            "return $s";
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQExpression expression = connection.createExpression();
                    XQResultSequence result = expression.executeQuery(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Unsafe when Using String Placeholders on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses string placeholders
             * makes it vulnerable to injection
             */
            case "saxonunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = String.format("for $s in doc(\"%s\")/Students/Student " +
                            "where $s/FirstName = \"%s\" " +
                            "return $s", xmlPath, request.getParameter("payload"));
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQExpression expression = connection.createExpression();
                    XQResultSequence result = expression.executeQuery(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Using Bind Variables on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses bind variables makes
             * it safe from injection
             */
            case "saxonsafebind": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "declare variable $path as xs:string external; " +
                            "declare variable $name as xs:string external; " +
                            "for $s in doc($path)/Students/Student " +
                            "where $s/FirstName = $name " +
                            "return $s";
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQExpression expression = connection.createExpression();
                    expression.bindString(new QName("path"), xmlPath, null);
                    expression.bindString(new QName("name"), request.getParameter("payload"), null);
                    XQResultSequence result = expression.executeQuery(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Escaping Quotation Marks and Semicolons on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses character escaping
             * makes it safe from injection
             */
            case "saxonsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String newPayload = (request.getParameter("payload").replace(";", "&#59"))
                            .replace("\"", "&quot;");
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + newPayload + "\" " +
                            "return $s";
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQExpression expression = connection.createExpression();
                    XQResultSequence result = expression.executeQuery(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Unsafe when Using String Concatenation on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses string
             * concatenation makes it vulnerable to injection
             */
            case "saxonunsafeconcatprep": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                            "return $s";
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQPreparedExpression expression = connection.prepareExpression(query);
                    XQResultSequence result = expression.executeQuery();
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Unsafe when Using String Placeholders on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses string
             * placeholders makes it vulnerable to injection
             */
            case "saxonunsafeplaceholderprep": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = String.format("for $s in doc(\"%s\")/Students/Student " +
                            "where $s/FirstName = \"%s\" " +
                            "return $s", xmlPath, request.getParameter("payload"));
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQPreparedExpression expression = connection.prepareExpression(query);
                    XQResultSequence result = expression.executeQuery();
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Using Bind Variables on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses bind
             * variables makes it safe from injection
             */
            case "saxonsafebindprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "declare variable $path as xs:string external; " +
                            "declare variable $name as xs:string external; " +
                            "for $s in doc($path)/Students/Student " +
                            "where $s/FirstName = $name " +
                            "return $s";
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQPreparedExpression expression = connection.prepareExpression(query);
                    expression.bindString(new QName("path"), xmlPath, null);
                    expression.bindString(new QName("name"), request.getParameter("payload"), null);
                    XQResultSequence result = expression.executeQuery();
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

                } catch (Exception ex) {
                    response.getWriter().write("<html><body>");
                    response.getWriter().write(ex.toString());
                    response.getWriter().write("</body></html>");
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to execute an XQuery expression that uses character
             * escaping makes it safe from injection
             */
            case "saxonsafeescapeprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String newPayload = (request.getParameter("payload").replace(";", "&#59"))
                            .replace("\"", "&quot;");
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + newPayload + "\" " +
                            "return $s";
                    XQDataSource dataSource = new SaxonXQDataSource();
                    XQConnection connection = dataSource.getConnection();
                    XQPreparedExpression expression = connection.prepareExpression(query);
                    XQResultSequence result = expression.executeQuery();
                    ArrayList<String> resultList = new ArrayList<>();
                    while (result.next()) {
                        resultList.add(result.getItemAsString(null));
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                    result.close();
                    expression.close();
                    connection.close();

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
     * Prints the results of the XQuery test case
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param resultList	the result of the XQuery query
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    private void printResults(boolean expectedSafe, ArrayList<String> resultList, HttpServletResponse response) throws IOException {

        boolean actuallySafe = true;
        if (resultList.size() > 1) {
            actuallySafe = false;
        }

        response.getWriter().write("<html><head><title>Results</title></head><body><h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XQuery query is safe! :)" : "Unsafe query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student or empty result" : "Should be all Students") + "):</b><br /><pre>");

        for (String student : resultList) {
            response.getWriter().write(Encode.forHtmlContent(student) + "<br />");
        }

        response.getWriter().write("</pre><br /><br /><br /><a href=\"xquery.jsp\">&lt&lt&lt back to tests</a>");
        response.getWriter().write("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
