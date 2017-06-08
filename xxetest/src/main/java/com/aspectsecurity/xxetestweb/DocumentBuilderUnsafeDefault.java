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
@WebServlet("/documentbuilderunsafedefault")
public class DocumentBuilderUnsafeDefault extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean expectedSafe = false;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentBuilderUnsafeDefault() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("X-Frame-Options", "DENY");
		
		String curDir = System.getProperty("user.dir");
		File GradeList = new File("src/main/resources/xxe.txt");
		System.out.println("Current sys dir: " + curDir);
		System.out.println("Current abs dir: " + GradeList.getAbsolutePath());
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
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
