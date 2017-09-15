package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetestweb.XXETestCase;
import com.aspectsecurity.xxetestweb.XMLTestBean;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@WebServlet("/xmldecoder")
public class XMLDecoderTestCase extends XXETestCase {

    /*
     * XMLDecoder: Always Safe (Always Unsafe in Java 1.7u45 and earlier) Example
     * Proves that XMLDecoder parses entities in Java versions 1.7u45 and earlier and does not in Java versions
     * 1.7u51 and later.
     * In Java versions 1.7u45 and earlier, the com.sun.beans.decoder.DocumentHandler that belongs to the
     * XMLDecoder object doesn't override the resolveEntity() method from the org.xml.sax.helpers.DefaultHandler
     * class (which is implemented from the org.xml.sax.EntityResolver interface), where the default behavior
     * resolve entities. In Java versions 1.7u51 and later, this method is implemented with a blank InputSource,
     * causing it to ignore the resolution of entities.
     * Since this is purely based in the XMLDecoder source code, there is no way to make it safe from malicious
     * entities in Java 1.7u45 and earlier, and no way to force it to be unsafe in Java 1.7u51 and later.
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean expectedSafe = true;
        if ((getJavaVersionMajor() == 7 && getJavaVersionUpdate() <= 45) || getJavaVersionMajor() <= 6) {
            expectedSafe = false;
        }

        // parsing the XML
        try {
            XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(request.getParameter("payload").getBytes()));
            XMLTestBean result = (XMLTestBean) decoder.readObject();
            decoder.close();

            // testing the result
            printResults(expectedSafe, result.getElement(), response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
