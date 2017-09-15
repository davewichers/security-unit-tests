package com.aspectsecurity.xxetestweb.xpathtestcases;

import com.aspectsecurity.xxetestweb.XPathTestCase;
import net.sf.saxon.s9api.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saxonunsafeconcat")
public class SaxonUnsafeConcatTestCase extends XPathTestCase {

    /*
     * Saxon: Unsafe when Using String Concatenation on XPath Expression Example
     * Proves that Saxon is vulnerable to injection when using string concatenation on the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        try {
            // parsing the XML
            Processor processor = new Processor(false);
            net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
            XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

            // querying the XML
            XPathCompiler compiler = processor.newXPathCompiler();
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
            XPathExecutable executable = compiler.compile(query);
            XPathSelector selector = executable.load();
            selector.setContextItem(node);
            selector.evaluate();

            ArrayList<String> resultList = new ArrayList<String>();
            for (XdmValue value : selector) {
                resultList.add(value.toString());
            }

            // testing the result
            printResults(expectedSafe, resultList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
