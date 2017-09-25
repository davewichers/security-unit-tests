package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.jaxb.BookType;
import com.aspectsecurity.unittests.jaxb.Collection;
import com.aspectsecurity.unittestsweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@WebServlet("/jaxbunsafexmlinputfactory")
public class JAXBUnsafeXMLInputFactoryTestCase extends XXETestCase {

    /*
     * JAXBContext: Unsafe JAXBContext Unmarshaller from Unsafe XMLInputFactory Example
     * Proves that XML deserialized using a JAXB Unmarshaller is unsafe from malicious entities when
     * unmarshalled through an XMLStreamReader from an unsafe XMLInputFactory
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = false;

        // parsing the XML
        try {

            JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.unittests.jaxb");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            //factory.setProperty("javax.xml.stream.supportDTD", true);	// this is the default

            Collection collection = (Collection)
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
