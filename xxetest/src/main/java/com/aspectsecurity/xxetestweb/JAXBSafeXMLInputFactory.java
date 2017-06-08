package com.aspectsecurity.xxetestweb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.owasp.encoder.Encode;
import org.xml.sax.SAXException;


import com.aspectsecurity.xxetest.jaxb.BookType;
import com.aspectsecurity.xxetest.jaxb.Collection;

/**
 * Servlet implementation class XXETesting
 */
@WebServlet("/jaxbsafexmlinputfactory")
public class JAXBSafeXMLInputFactory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean expectedSafe = true;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public JAXBSafeXMLInputFactory() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("X-Frame-Options", "DENY");
		
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jc.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty("javax.xml.stream.supportDTD", false);
        
        response.getWriter().write("<html><head><title>Results</title></head><body><span style=\"white-space: pre\">");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe\n" : "Unsafe\n") + "Actual Result: ");
        try {
        	Collection collection= (Collection)
	                 unmarshaller.unmarshal(factory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes())));
       	
	        Collection.Books booksType = collection.getBooks();
	        List<BookType> bookList = booksType.getBook();
	
	        String discount = "";
	        for( int i = 0; i < bookList.size();i++ ) {
	        	BookType book =(BookType) bookList.get(i);           
	            discount = book.getPromotion().getDiscount().trim();
	            System.out.println("Book promotion default: " +  discount);
	        }
	
	        if (discount.contains("SUCCESS"))
	        	response.getWriter().write("XXE was injected! :(\n\nXML Content (Should contain \"SUCCESSFUL\"):\n" + Encode.forHtmlContent(discount));
	        	
        } catch (Exception ex) {
			response.getWriter().write("XML Parser is safe! :)\n\nStack Trace:\n");
			ex.printStackTrace(response.getWriter());
        }
        finally {
        	response.getWriter().write("</span></body></html>");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
