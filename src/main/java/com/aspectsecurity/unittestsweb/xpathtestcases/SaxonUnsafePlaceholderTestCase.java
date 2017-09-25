package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.XPathTestCase;
import net.sf.saxon.s9api.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saxonunsafeplaceholder")
public class SaxonUnsafePlaceholderTestCase extends XPathTestCase {

    /*
     * Saxon: Unsafe when Using String Placeholders on XPath Expression Example
     * Proves that Saxon is vulnerable to injection when using string placeholders on the XPath expression
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
            String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
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
