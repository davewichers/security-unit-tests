package com.aspectsecurity.xxetestweb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.owasp.encoder.Encode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import com.aspectsecurity.xxetest.SAXHandler;
import com.aspectsecurity.xxetest.jaxb.BookType;
import com.aspectsecurity.xxetest.jaxb.Collection;

/**
 * Servlet implementation class XXETesting
 */
@WebServlet("/transformersafeaccess")
public class TransformerSafeAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean expectedSafe = true;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransformerSafeAccess() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("X-Frame-Options", "DENY");
		
		TransformerFactory factory = TransformerFactory.newInstance();
		System.out.println("Current3a TransformerFactory ACCESS_EXTERNAL_DTD property value: '" 
				+ factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		// TODO: Report to Xerces that this property value is null after setting it to an empty string, even 
		// though it still works. (Confirm this).
		if (factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) == null)
			System.out.println("DRW: XMLConstants.ACCESS_EXTERNAL_DTD property value is null");
		System.out.println("Current3b TransformerFactory ACCESS_EXTERNAL_DTD property value: '" 
				+ factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
        Transformer transformer = null;
		try {
			transformer = factory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // not passing in stylesheet
        
        response.getWriter().write("<html><head><title>Results</title></head><body><span style=\"white-space: pre\">");
		response.getWriter().write("Expected result: " + (expectedSafe ? "Safe\n" : "Unsafe\n") + "Actual Result: ");
        try {
        	
        	StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        StreamResult output = new StreamResult(baos);
	        
	        transformer.transform(input, output);
	        String result = new String(baos.toByteArray());
	        //System.out.println("TransformerFactory result: " + result);	
	
	        if (result.contains("SUCCESSFUL"))
	        	response.getWriter().write("XXE was injected! :(\n\nXML Content (Should contain \"SUCCESSFUL\"):\n" + Encode.forHtmlContent(result));
	        else
	        	response.getWriter().write("XML Parser is safe! :) \n\nXML Content: " + Encode.forHtmlContent(result));
	        	
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
