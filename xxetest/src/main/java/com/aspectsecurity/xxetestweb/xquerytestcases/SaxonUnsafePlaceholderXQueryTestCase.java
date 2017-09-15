package com.aspectsecurity.xxetestweb.xquerytestcases;

import com.aspectsecurity.xxetestweb.XQueryTestCase;
import net.sf.saxon.s9api.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saxonunsafeplaceholderxquery")
public class SaxonUnsafePlaceholderXQueryTestCase extends XQueryTestCase {

    /*
     * Saxon: Unsafe when Using String Placeholders on XQuery Expression Example
     * Proves that Saxon is vulnerable to injection when using string placeholders on the XQuery expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
            ArrayList<String> resultList = new ArrayList<String>();
            for (XdmValue value : xqEval) {
                resultList.add(value.toString());
            }

            // testing the result
            printResults(expectedSafe, resultList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
