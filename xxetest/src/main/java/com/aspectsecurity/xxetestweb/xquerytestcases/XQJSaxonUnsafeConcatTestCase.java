package com.aspectsecurity.xxetestweb.xquerytestcases;

import com.aspectsecurity.xxetestweb.XQueryTestCase;
import com.saxonica.xqj.SaxonXQDataSource;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/xqjsaxonunsafeconcat")
public class XQJSaxonUnsafeConcatTestCase extends XQueryTestCase {

    /*
     * XQJ with Saxon: Unsafe when Using String Concatenation on XQExpression Example
     * Proves that using the XQExpression class to execute an XQuery expression that uses string concatenation
     * makes it vulnerable to injection
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
            ArrayList<String> resultList = new ArrayList<String>();
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
    }
}
