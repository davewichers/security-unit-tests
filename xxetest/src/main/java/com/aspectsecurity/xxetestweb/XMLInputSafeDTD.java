package com.aspectsecurity.xxetestweb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
import javax.xml.stream.XMLStreamReader;
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
@WebServlet("/xmlinputsafedtd")
public class XMLInputSafeDTD extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean expectedSafe = true;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public XMLInputSafeDTD() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("X-Frame-Options", "DENY");
		
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		
		response.getWriter().write("<html><head><title>Results</title></head><body><span style=\"white-space: pre\">");
		response.getWriter().write("Expected result: " + (expectedSafe ? "Safe\n" : "Unsafe\n") + "Actual Result: ");
        try {
        	
        	XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));
			
			while(xmlStreamReader.hasNext()){
				xmlStreamReader.next();
			    if(xmlStreamReader.isStartElement()){
			        if (xmlStreamReader.getLocalName().equals("test")) {
			        	String result = xmlStreamReader.getElementText();
			        	if (result.equals("SUCCESSFUL"))
				        	response.getWriter().write("XXE was injected! :(\n\nXML Content (Should contain \"SUCCESSFUL\"):\n" + Encode.forHtmlContent(result));
				        else
				        	response.getWriter().write("XML Parser is safe! :) \n\nXML Content: " + Encode.forHtmlContent(result));
			        }
			    }
			}
	        	
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
