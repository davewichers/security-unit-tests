package com.aspectsecurity.xxetestweb.xxetestcases;

import com.aspectsecurity.xxetest.jaxb.BookType;
import com.aspectsecurity.xxetest.jaxb.Collection;
import com.aspectsecurity.xxetestweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@WebServlet("/jaxbsafexmlinputfactory")
public class JAXBSafeXMLInputFactoryTestCase extends XXETestCase {

    /*
     * JAXBContext: Safe JAXBContext Unmarshaller from Safe XMLInputFactory Example
     * Proves that XML deserialized using a JAXB Unmarshaller is safe from malicious entities when
     * unmarshalled through an XMLStreamReader from a safe XMLInputFactory. It does throw by throwing an
     * exception when seeing an external entity reference.
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        // parsing the XML
        try {

            JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty("javax.xml.stream.supportDTD", false);

            Collection collection= (Collection)
                    unmarshaller.unmarshal(factory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

            Collection.Books booksType = collection.getBooks();
            List<BookType> bookList = booksType.getBook();

            String discount = "";
            for (BookType book : bookList) {
                discount = book.getPromotion().getDiscount().trim();
            }

            // testing the result
            printResults(expectedSafe, discount, response);
        }
        catch (Exception ex) {
            printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
        }
    }
}
