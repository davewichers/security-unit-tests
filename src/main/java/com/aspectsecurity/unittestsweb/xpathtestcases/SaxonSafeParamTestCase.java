package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.XPathTestCase;
import net.sf.saxon.s9api.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saxonsafeparam")
public class SaxonSafeParamTestCase extends XPathTestCase {

    /*
     * Saxon: Safe when Parameterizing on XPath Expression Example
     * Proves that Saxon is safe from injection when using a QName to parameterize the XPath expression
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
            net.sf.saxon.s9api.QName qname = new net.sf.saxon.s9api.QName("name");
            compiler.declareVariable(qname);
            String query = "/Students/Student[FirstName/text()=$name]"; // safe!
            XPathExecutable executable = compiler.compile(query);
            XPathSelector selector = executable.load();
            selector.setContextItem(node);
            selector.setVariable(qname, XdmValue.makeValue(request.getParameter("payload")));
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
