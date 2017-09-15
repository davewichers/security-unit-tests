package com.aspectsecurity.xxetestweb.xpathtestcases;

import com.aspectsecurity.xxetestweb.XPathTestCase;
import net.sf.saxon.s9api.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saxonsafeescape")
public class SaxonSafeEscapeTestCase extends XPathTestCase {

    /*
     * Saxon: Safe when Escaping Apostrophes on XPath Expression Example
     * Proves that Saxon is safe from injection when using string concatenation while escaping apostrophes on
     * the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // parsing the XML
            Processor processor = new Processor(false);
            net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
            XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

            // querying the XML
            XPathCompiler compiler = processor.newXPathCompiler();
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
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
