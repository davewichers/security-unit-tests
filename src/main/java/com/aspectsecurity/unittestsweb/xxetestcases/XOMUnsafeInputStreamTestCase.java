package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittestsweb.XXETestCase;
import nu.xom.Builder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/xomunsafeinputstream")
public class XOMUnsafeInputStreamTestCase extends XXETestCase {

    /*
     * XOM: Unsafe when using an InputStream Example
     * Proves that when building the XOM Document from an unsafe XML InputStream, the Document parses the DTD
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final boolean expectedSafe = false;

        // parsing the XML
        try {
            Builder builder = new Builder();
            nu.xom.Document doc = builder.build(new ByteArrayInputStream(request.getParameter("payload").getBytes()));	// unsafe!

            // testing the result
            printResults(expectedSafe, doc.toXML(), response);

        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
