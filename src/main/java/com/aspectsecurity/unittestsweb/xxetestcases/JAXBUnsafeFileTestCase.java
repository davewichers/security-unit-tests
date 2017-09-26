package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.jaxb.BookType;
import com.aspectsecurity.unittests.jaxb.Collection;
import com.aspectsecurity.unittestsweb.XXETestCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@WebServlet("/jaxbunsafefile")
public class JAXBUnsafeFileTestCase extends XXETestCase {

    /*
     * JAXBContext: Unsafe (Safe in Java 1.8 and up) JAXBContext Unmarshaller from File Example
     * Proves that XML deserialized using a JAXB Unmarshaller is unsafe from malicious entities when
     * unmarshalled directly from File (Safe in 1.8 and up)
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean expectedSafe = false;
        if (getJavaVersionMajor() >= 8) {
            expectedSafe = true;
        }

        // parsing the XML
        try {

            JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.unittests.jaxb");
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            Collection collection= (Collection)
                    unmarshaller.unmarshal(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

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
