package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.InvalidParameterException;
import com.aspectsecurity.unittestsweb.XPathTestCase;
import net.sf.saxon.s9api.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saxonsafelist")
public class SaxonSafeListTestCase extends XPathTestCase {

    /*
     * Saxon: Safe when Whitelisting on XPath Expression Example
     * Proves that Saxon is safe from injection when whitelisting on the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // parsing the XML
            Processor processor = new Processor(false);
            net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
            XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

            // querying the XML
            String query;
            if (request.getParameter("payload").contains("'")) {
                printResults(expectedSafe, new ArrayList<String>(), response);
                throw new InvalidParameterException("First Name parameter must not contain apostrophes");
            }
            else {
                query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
            }
            XPathCompiler compiler = processor.newXPathCompiler();
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
