package com.aspectsecurity.xxetestweb;

import com.saxonica.xqj.SaxonXQDataSource;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.xqj.basex.BaseXXQDataSource;
import org.owasp.encoder.Encode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
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

        response.getWriter().write("<html><head><title>Results</title></head><body>");

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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            /* ********************************************************************************************************
             * XQJ with com.saxonica.xqj.SaxonXQDataSource (Saxonica Saxon9)
             * ********************************************************************************************************/
            //region XQJ with Saxon: Unsafe when Using String Concatenation on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses string concatenation
             * makes it vulnerable to injection
             */
            case "xqjsaxonunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                            "return $s";    // unsafe!
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

                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Unsafe when Using String Placeholders on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses string placeholders
             * makes it vulnerable to injection
             */
            case "xqjsaxonunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = String.format("for $s in doc(\"%s\")/Students/Student " +
                            "where $s/FirstName = \"%s\" " +
                            "return $s", xmlPath, request.getParameter("payload")); // unsafe!
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Using Bind Variables on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses bind variables makes
             * it safe from injection
             */
            case "xqjsaxonsafebind": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "declare variable $path as xs:string external; " +
                            "declare variable $name as xs:string external; " +
                            "for $s in doc($path)/Students/Student " +
                            "where $s/FirstName = $name " +
                            "return $s";    // safe!
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Whitelisting on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses whitelisting makes it
             * safe from injection
             */
            case "xqjsaxonsafelist": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query;
                    if (request.getParameter("payload").contains("\"") || request.getParameter("payload").contains(";")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain quotes or semicolons");
                    }
                    else {
                        query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                                "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                                "return $s";  // safe in here!
                    }
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

                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Escaping Quotation Marks and Semicolons on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses character escaping
             * makes it safe from injection
             */
            case "xqjsaxonsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String newPayload = (request.getParameter("payload").replace(";", "&#59"))
                            .replace("\"", "&quot;");
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + newPayload + "\" " +
                            "return $s";    // safe!
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Unsafe when Using String Concatenation on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses string
             * concatenation makes it vulnerable to injection
             */
            case "xqjsaxonunsafeconcatprep": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                            "return $s";    // unsafe!
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Unsafe when Using String Placeholders on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses string
             * placeholders makes it vulnerable to injection
             */
            case "xqjsaxonunsafeplaceholderprep": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = String.format("for $s in doc(\"%s\")/Students/Student " +
                            "where $s/FirstName = \"%s\" " +
                            "return $s", xmlPath, request.getParameter("payload")); // unsafe!
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Using Bind Variables on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses bind
             * variables makes it safe from injection
             */
            case "xqjsaxonsafebindprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "declare variable $path as xs:string external; " +
                            "declare variable $name as xs:string external; " +
                            "for $s in doc($path)/Students/Student " +
                            "where $s/FirstName = $name " +
                            "return $s";    // safe!
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Whitelisting on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses whitelisting
             * makes it safe from injection
             */
            case "xqjsaxonsafelistprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query;
                    if (request.getParameter("payload").contains("\"") || request.getParameter("payload").contains(";")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain quotes or semicolons");
                    }
                    else {
                        query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                                "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                                "return $s";  // safe in here!
                    }
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with Saxon: Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to execute an XQuery expression that uses character
             * escaping makes it safe from injection
             */
            case "xqjsaxonsafeescapeprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String newPayload = (request.getParameter("payload").replace(";", "&#59"))
                            .replace("\"", "&quot;");
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + newPayload + "\" " +
                            "return $s";    // safe!
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            /* ********************************************************************************************************
             * XQJ with net.xqj.basex.BaseXXQDataSource (BaseX)
             * ********************************************************************************************************/
            //region XQJ with BaseX: Unsafe when Using String Concatenation on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses string concatenation
             * makes it vulnerable to injection
             */
            case "xqjbasexunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                            "return $s";    // unsafe!
                    XQDataSource dataSource = new BaseXXQDataSource();
                    dataSource.setProperty("user", "admin");
                    dataSource.setProperty("password", "admin");
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Unsafe when Using String Placeholders on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses string placeholders
             * makes it vulnerable to injection
             */
            case "xqjbasexunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = String.format("for $s in doc(\"%s\")/Students/Student " +
                            "where $s/FirstName = \"%s\" " +
                            "return $s", xmlPath, request.getParameter("payload")); // unsafe!
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Safe when Using Bind Variables on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses bind variables makes
             * it safe from injection
             */
            case "xqjbasexsafebind": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "declare variable $path as xs:string external; " +
                            "declare variable $name as xs:string external; " +
                            "for $s in doc($path)/Students/Student " +
                            "where $s/FirstName = $name " +
                            "return $s";    // safe!
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Safe when Whitelisting on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses whitelisting makes it
             * safe from injection
             */
            case "xqjbasexsafelist": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query;
                    if (request.getParameter("payload").contains("\"") || request.getParameter("payload").contains(";")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain quotes or semicolons");
                    }
                    else {
                        query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                                "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                                "return $s";  // safe in here!
                    }
                    XQDataSource dataSource = new BaseXXQDataSource();
                    dataSource.setProperty("user", "admin");
                    dataSource.setProperty("password", "admin");
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Safe when Escaping Quotation Marks and Semicolons on XQExpression Example
            /*
             * Proves that using the XQExpression class to execute an XQuery expression that uses character escaping
             * makes it safe from injection
             */
            case "xqjbasexsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String newPayload = (request.getParameter("payload").replace(";", "&#59"))
                            .replace("\"", "&quot;");
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + newPayload + "\" " +
                            "return $s";    // safe!
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Unsafe when Using String Concatenation on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses string
             * concatenation makes it vulnerable to injection
             */
            case "xqjbasexunsafeconcatprep": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                            "return $s";    // unsafe!
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Unsafe when Using String Placeholders on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses string
             * placeholders makes it vulnerable to injection
             */
            case "xqjbasexunsafeplaceholderprep": {
                final boolean expectedSafe = false;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = String.format("for $s in doc(\"%s\")/Students/Student " +
                            "where $s/FirstName = \"%s\" " +
                            "return $s", xmlPath, request.getParameter("payload")); // unsafe!
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Safe when Using Bind Variables on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses bind
             * variables makes it safe from injection
             */
            case "xqjbasexsafebindprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query = "declare variable $path as xs:string external; " +
                            "declare variable $name as xs:string external; " +
                            "for $s in doc($path)/Students/Student " +
                            "where $s/FirstName = $name " +
                            "return $s";    // safe!
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Safe when Whitelisting on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses whitelisting
             * makes it safe from injection
             */
            case "xqjbasexsafelistprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String query;
                    if (request.getParameter("payload").contains("\"") || request.getParameter("payload").contains(";")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain quotes or semicolons");
                    }
                    else {
                        query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                                "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                                "return $s";  // safe in here!
                    }
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region XQJ with BaseX: Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example
            /*
             * Proves that using the XQPreparedExpression class to execute an XQuery expression that uses character
             * escaping makes it safe from injection
             */
            case "xqjbasexsafeescapeprep": {
                final boolean expectedSafe = true;

                try {
                    // querying the XML
                    String xmlPath = getClass().getResource("/students.xml").toString();
                    String newPayload = (request.getParameter("payload").replace(";", "&#59"))
                            .replace("\"", "&quot;");
                    String query = "for $s in doc(\"" + xmlPath + "\")/Students/Student " +
                            "where $s/FirstName = \"" + newPayload + "\" " +
                            "return $s";    // safe!
                    XQDataSource dataSource = new BaseXXQDataSource();
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
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            /* ********************************************************************************************************
             * net.sf.saxon.s9api (Saxonica Saxon9)
             * ********************************************************************************************************/
            //region Saxon: Unsafe when Using String Concatenation on XQuery Expression Example
            /*
             * Proves that Saxon is vulnerable to injection when using string concatenation on the XQuery expression
             */
            case "saxonunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    String query = "for $s in //Students/Student " +
                            "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                            "return $s";  // unsafe!
                    XQueryCompiler xqComp = processor.newXQueryCompiler();
                    XQueryExecutable xqExec = xqComp.compile(query);
                    XQueryEvaluator xqEval = xqExec.load();
                    xqEval.setContextItem(node);
                    xqEval.evaluate();

                    // interpret the result of the query
                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : xqEval) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Unsafe when Using String Placeholders on XQuery Expression Example
            /*
             * Proves that Saxon is vulnerable to injection when using string placeholders on the XQuery expression
             */
            case "saxonunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    String query = String.format("for $s in //Students/Student " +
                            "where $s/FirstName = \"%s\" " +
                            "return $s", request.getParameter("payload"));  // unsafe!
                    XQueryCompiler xqComp = processor.newXQueryCompiler();
                    XQueryExecutable xqExec = xqComp.compile(query);
                    XQueryEvaluator xqEval = xqExec.load();
                    xqEval.setContextItem(node);
                    xqEval.evaluate();

                    // interpret the result of the query
                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : xqEval) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Unsafe when Using Bind Variables on XQuery Expression Example
            /*
             * Proves that Saxon is safe from injection when using bind variables on the XQuery expression
             */
            case "saxonsafebind": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    String query = "declare variable $name as xs:string external; " +
                            "for $s in //Students/Student " +
                            "where $s/FirstName = $name " +
                            "return $s";  // unsafe!
                    XQueryCompiler xqComp = processor.newXQueryCompiler();
                    XQueryExecutable xqExec = xqComp.compile(query);
                    XQueryEvaluator xqEval = xqExec.load();
                    xqEval.setContextItem(node);
                    xqEval.setExternalVariable(new net.sf.saxon.s9api.QName("name"), new XdmAtomicValue(request.getParameter("payload")));
                    xqEval.evaluate();

                    // interpret the result of the query
                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : xqEval) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Safe when Whitelisting on XQuery Expression Example
            /*
             * Proves that Saxon is safe from injection when whitelisting on the XQuery expression
             */
            case "saxonsafelist": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    String query;
                    if (request.getParameter("payload").contains("\"") || request.getParameter("payload").contains(";")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain quotes or semicolons");
                    }
                    else {
                        query = "for $s in //Students/Student " +
                                "where $s/FirstName = \"" + request.getParameter("payload") + "\" " +
                                "return $s";  // safe in here!
                    }
                    XQueryCompiler xqComp = processor.newXQueryCompiler();
                    XQueryExecutable xqExec = xqComp.compile(query);
                    XQueryEvaluator xqEval = xqExec.load();
                    xqEval.setContextItem(node);
                    xqEval.evaluate();

                    // interpret the result of the query
                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : xqEval) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());

                }

                break;
            }
            //endregion

            //region Saxon: Safe when Escaping Quotation Marks and Semicolons on XQuery Expression Example
            /**
             * Proves that Saxon is safe from injection when using character escaping on the XQuery expression
             */
            case "saxonsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    String newPayload = (request.getParameter("payload").replace(";", "&#59")).replace("\"", "&quot;");
                    String query = "for $s in //Students/Student " +
                            "where $s/FirstName = \"" + newPayload + "\" " +
                            "return $s";  // safe!
                    XQueryCompiler xqComp = processor.newXQueryCompiler();
                    XQueryExecutable xqExec = xqComp.compile(query);
                    XQueryEvaluator xqEval = xqExec.load();
                    xqEval.setContextItem(node);
                    xqEval.evaluate();

                    // interpret the result of the query
                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : xqEval) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            default:
                response.getWriter().write("Error: Test case not found for \"" + request.getParameter("var") + "\"");
                break;
        }

        response.getWriter().write("<br /><br /><br /><a href=\"xquery.jsp\">&lt&lt&lt back to tests</a>");
        response.getWriter().write("</body></html>");
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

        response.getWriter().write("<h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XQuery query is safe! :)" : "Unsafe query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student or empty result" : "Should be all Students") + "):</b><br /><pre>");

        for (String student : resultList) {
            response.getWriter().write(Encode.forHtmlContent(student) + "<br />");
        }

        response.getWriter().write("</pre>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
