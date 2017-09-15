package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;
import nu.xom.Builder;
import nu.xom.DocType;
import nu.xom.Element;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/xomsafe")
public class XOMSafe extends XXETestCase {

    /*
     * XOM: Safe by Default Example
     * Proves that XOM's built in methods do not have the ability to add an external entity reference.
     * I emailed Elliote Rusty Harold, creator of XOM, about this test case and he said, "There's no way to
     * insert an entity reference directly in XOM." Therefore, XOM is safe from injection if using the methods
     * found in this test.
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final boolean expectedSafe = true;

        // creating the parser
        Element test = new Element(request.getParameter("tag"));
        test.appendChild(request.getParameter("ref"));	// appends text, not an actual entity reference, making this safe
        nu.xom.Document doc = new nu.xom.Document(test);
        String dtd = request.getParameter("doc");
        Builder builder = new Builder();
        nu.xom.Document newDoc;

        // parsing the XML
        try {
            newDoc = builder.build(dtd, null);
            DocType doctype = newDoc.getDocType();
            doctype.detach();
            doc.setDocType(doctype);

            // testing the result
            printResults(expectedSafe, doc.toXML(), response);

        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
