package com.aspectsecurity.xxetestweb.xquerytestcases;

import com.aspectsecurity.xxetestweb.XQueryTestCase;
import net.sf.saxon.s9api.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saxonsafebindxquery")
public class SaxonSafeBindXQueryTestCase extends XQueryTestCase {

    /*
     * Saxon: Unsafe when Using Bind Variables on XQuery Expression Example
     * Proves that Saxon is safe from injection when using bind variables on the XQuery expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
