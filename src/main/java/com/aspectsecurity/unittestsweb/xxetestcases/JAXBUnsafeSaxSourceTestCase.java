package com.aspectsecurity.unittestsweb.xxetestcases;

import com.aspectsecurity.unittests.jaxb.BookType;
import com.aspectsecurity.unittests.jaxb.Collection;
import com.aspectsecurity.unittestsweb.XXETestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@WebServlet("/jaxbunsafesaxsource")
public class JAXBUnsafeSaxSourceTestCase extends XXETestCase {


    @Override
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final boolean expectedSafe = false;
        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(Collection.class).createUnmarshaller();
            SAXSource saxSource = new SAXSource(
                    SAXParserFactory.newInstance().newSAXParser().getXMLReader(),
                    new InputSource(new ByteArrayInputStream(request.getParameter("payload").getBytes())));
            Collection collection = unmarshaller.unmarshal(saxSource, Collection.class).getValue();
            Collection.Books booksType = collection.getBooks();
            List<BookType> bookList = booksType.getBook();
            String discount = "";
            for (BookType book : bookList) {
                discount = book.getPromotion().getDiscount().trim();
            }
            printResults(expectedSafe, discount, response);
        }
        catch (Exception ex) {
            printResults(true, ex, response);	// safe: exception thrown when parsing XML
        }
    }




}
