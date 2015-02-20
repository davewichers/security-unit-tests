package com.aspectsecurity.xxetest;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.CodeSource;
import java.text.MessageFormat;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	public static final String javaVersion = System.getProperty("java.version");
	public static boolean setSafeXMLProperties = false;
	public static final String JAVARUNTIME = "Java_Runtime";
	
	public static String domLocation = "";
	public static String saxLocation = "";
	
	static {
		System.out.println("Java version is: " + javaVersion);
		
		OutputJaxpImplementationInfo();
		
		System.out.println("Default javax.xml.accessExternalDTD value: '" 
			+ System.getProperty("javax.xml.accessExternalDTD") + "'");
		System.out.println("Default javax.xml.accessExternalSchema value: '" 
			+ System.getProperty("javax.xml.accessExternalSchema") + "'");
		System.out.println("Default javax.xml.accessExternalStylesheet value: '" 
			+ System.getProperty("javax.xml.accessExternalStylesheet") + "'");
		
		if (setSafeXMLProperties) {
			System.setProperty("javax.xml.accessExternalDTD", "");
			System.setProperty("javax.xml.accessExternalSchema", "");
			System.setProperty("javax.xml.accessExternalStylesheet", "");
			System.out.println("Secure XML Processing system properties set to:");
			System.out.println("  Current javax.xml.accessExternalDTD value: '" 
				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
			System.out.println("  Current javax.xml.accessExternalSchema value: '" 
				+ System.getProperty("javax.xml.accessExternalSchema") + "'");
			System.out.println("  Current javax.xml.accessExternalStylesheet value: '" 
				+ System.getProperty("javax.xml.accessExternalStylesheet") + "'");
			
		} else {
			
			// Let's also consider setSafeXMLProperties to be set if the jaxp.properties file exists and
			// the following property is set like so: javax.xml.accessExternalDTD=
			String javaHome = System.getProperty("java.home");
			String fileSeperator = System.getProperty("file.separator");
			String jaxpPropertiesFile = javaHome + fileSeperator + "lib" + fileSeperator + "jaxp.properties";
			File JAXPFile = new File(jaxpPropertiesFile);
			if (JAXPFile.exists())
			{
				Properties jaxpProps = new Properties();
				try {
					InputStream propin = new FileInputStream(jaxpPropertiesFile);
					jaxpProps.load(propin);
					if ("".equals(jaxpProps.getProperty("javax.xml.accessExternalDTD")))
						// If the JAXP Properties are set to safe configuration, its equivalent to doing it manually like above
						setSafeXMLProperties = true;
				} catch (IOException ex) {
					System.out.println("Couldn't open jaxp.properties file: ");
					ex.printStackTrace();
				}
			} // end if
		} // end else
	}
	
	public static void OutputJaxpImplementationInfo() {
	    Class domParserClass = SAXParserFactory.newInstance().getClass();
	    CodeSource domSource = domParserClass.getProtectionDomain().getCodeSource();
	    domLocation = (domSource == null ? JAVARUNTIME : (String) domSource.getLocation().toString());
	    System.out.println(getJaxpImplementationInfo("DocumentBuilderFactory", domParserClass));
	    try {
	    	System.out.println(getJaxpImplementationInfo("JAXBContext", 
	    			JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb").getClass()));
	    } catch (Exception e) {
	    	System.out.println("Couldn't get JAXP implementation details for JAXBContext");
	    	e.printStackTrace();
	    }
	    
	    Class saxParserClass = SAXParserFactory.newInstance().getClass();
	    CodeSource saxSource = saxParserClass.getProtectionDomain().getCodeSource();
	    saxLocation = (saxSource == null ? JAVARUNTIME : (String) saxSource.getLocation().toString());
	    System.out.println(getJaxpImplementationInfo("SAXParserFactory", saxParserClass));
	    
	    System.out.println(getJaxpImplementationInfo("SchemaFactory", SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).getClass()));
	    System.out.println(getJaxpImplementationInfo("TransformerFactory", TransformerFactory.newInstance().getClass()));
	    System.out.println(getJaxpImplementationInfo("XMLInputFactory", XMLInputFactory.newInstance().getClass()));
	    try {
	    	System.out.println(getJaxpImplementationInfo("XMLReader", SAXParserFactory.newInstance().newSAXParser().getXMLReader().getClass()));
	    } catch (Exception e) {
	    	System.out.println("Couldn't get JAXP implementation details for XMLReader");
	    	e.printStackTrace();
	    }
	    System.out.println(getJaxpImplementationInfo("XPathFactory", XPathFactory.newInstance().getClass()));
	}

	public static String getJaxpImplementationInfo(String componentName, Class componentClass) {
	    CodeSource source = componentClass.getProtectionDomain().getCodeSource();
	    Package p = componentClass.getPackage();
	    return MessageFormat.format(
	            "{0} implementation: {1} ({2}) version {3} ({4}) loaded from: {5}",
	            componentName,
	            componentClass.getName(),
	            p.getImplementationVendor(),
	            p.getSpecificationVersion(),
	            p.getImplementationVersion(),
	            source == null ? JAVARUNTIME : source.getLocation());	    
	}
	
	public void testCreateDocumentBuilder() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setValidating(false);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
	}
	
	public void testCreateSAXParser() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
	}

	public void testCreateXMLInputFactory() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileReader("src/main/resources/xxetest1.xml"));
		assertTrue( xmlStreamReader != null );
	}
}
