package com.aspectsecurity.unittestsweb.xquerytestcases;

import com.aspectsecurity.unittestsweb.XQueryTestCase;
import com.saxonica.xqj.SaxonXQDataSource;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/xqjsaxonunsafeconcatprep")
public class XQJSaxonUnsafeConcatPrepTestCase extends XQueryTestCase {

    /*
     * XQJ with Saxon: Unsafe when Using String Concatenation on XQPreparedExpression Example
     * Proves that using the XQPreparedExpression class to prepare the XQuery expression that uses string
     * concatenation makes it vulnerable to injection
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
            XQPreparedExpression expression = connection.prepareExpression(query);
            XQResultSequence result = expression.executeQuery();
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
