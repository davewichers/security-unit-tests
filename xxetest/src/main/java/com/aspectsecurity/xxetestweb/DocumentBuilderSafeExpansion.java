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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.owasp.encoder.Encode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import com.aspectsecurity.xxetest.jaxb.BookType;
import com.aspectsecurity.xxetest.jaxb.Collection;

/**
 * Servlet implementation class XXETesting
 */
@WebServlet("/documentbuildersafeexpansion")
public class DocumentBuilderSafeExpansion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean expectedSafe = true;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentBuilderSafeExpansion() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("X-Frame-Options", "DENY");
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		System.out.println("Current DocumentBuilderFactory properties set to:");
		System.out.println("  Current isExpandEntityReferences value: '" 
			+ docBuilderFactory.isExpandEntityReferences() + "'");
		System.out.println("  Current isValidating value: '"
				+ docBuilderFactory.isValidating() + "'");
		System.out.println("  Current isCoalescing value: '"
				+ docBuilderFactory.isCoalescing() + "'");
		System.out.println("  Current isNamespaceAware value: '"
				+ docBuilderFactory.isNamespaceAware() + "'");
		System.out.println("  Current isXIncludeAware value: '"
				+ docBuilderFactory.isXIncludeAware() + "'");
		try {
			System.out.println("  Current http://xml.org/sax/features/external-general-entities feature value: '" 
					+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-general-entities") + "'");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("  Current http://xml.org/sax/features/external-parameter-entities feature value: '" 
					+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-parameter-entities") + "'");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			docBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// This doesn't seem to work, even though this value is indeed set to false
		// This is a Xerces implementation issue, not a Java Runtime issue
		// The Java Runtime version behaves the same
		//docBuilderFactory.setExpandEntityReferences(false);
		//docBuilderFactory.setFeature("http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities", false);
		
		// ... but this does work (per https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J)
		// ...... but that's in the test SetFeatureDisallowDoctypeDeclTest.java
		//docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
		System.out.println("After setting DocumentBuilderFactory to disable ExpandingEntityReferences:");
		System.out.println("  Current isExpandEntityReferences value: '" 
			+ docBuilderFactory.isExpandEntityReferences() + "'");
		try {
			System.out.println("  Current http://xml.org/sax/features/external-general-entities feature value: '" 
					+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-general-entities") + "'");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("  Current http://xml.org/sax/features/external-parameter-entities feature value: '" 
					+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-parameter-entities") + "'");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        response.getWriter().write("<html><head><title>Results</title></head><body><span style=\"white-space: pre\">");
		response.getWriter().write("Expected result: " + (expectedSafe ? "Safe\n" : "Unsafe\n") + "Actual Result: ");
        try {
        	Document doc = docBuilder.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()));
	
	        if (doc.getDocumentElement().getTextContent().contains("SUCCESS"))
	        	response.getWriter().write("XXE was injected! :(\n\nXML Content (Should contain \"SUCCESSFUL\"):\n" + Encode.forHtmlContent(doc.getDocumentElement().getTextContent()));
	        	
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
