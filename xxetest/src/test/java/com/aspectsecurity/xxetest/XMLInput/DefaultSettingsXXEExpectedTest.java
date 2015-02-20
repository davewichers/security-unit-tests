package com.aspectsecurity.xxetest.XMLInput;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import java.io.FileReader;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.aspectsecurity.xxetest.AppTest;

/*
 * DocumentBuilderFactory Test Cases
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultSettingsXXEExpectedTest extends TestCase {
	
	public void test1UnsafeXMLInputFactory() throws Exception {
		
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(
					new FileReader("src/main/resources/xxetest1.xml"));
			
			boolean reached = false;
			
			while(xmlStreamReader.hasNext()){
				xmlStreamReader.next();
			    if(xmlStreamReader.isStartElement()){
			        if (xmlStreamReader.getLocalName().equals("test")) {
			        	reached = true;
			        	assertTrue("The expected text was not loaded from the system file.", 
			        			xmlStreamReader.getElementText().equals("SUCCESSFUL"));
			        	//System.out.println(xmlStreamReader.getElementText());
			        }
			    }
			}
			
        	if (AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8")) {
        		fail("Setting the safe XXE processing system properties should prevent XXE with XMLInputFactory in Java 8");
        	}
        	else assertTrue( reached );
        	
        } catch (XMLStreamException ex) {
        	if (AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 with the system settings properly set prevents XXE for XMLInputFactory "
					+ " and returns the error: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property.");
        		System.out.println("Current1 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else {
        		System.out.println("Current2 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
        		System.out.println("Current2 xmlInputFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ xmlInputFactory.getProperty(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the TransformerFactory testLoadSystemFile test");
			}
   		}
	}


	public void test2ACCESS_EXTERNAL_DTDSupportInXMLInputFactory() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		
		if (AppTest.javaVersion.startsWith("1.8"))
			try {
				xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
				// TODO: Report to Sun that this property value is null after setting it to an empty string.
/*				if (xmlInputFactory.getProperty(XMLConstants.ACCESS_EXTERNAL_DTD) == null)
					System.out.println("DRW: XMLConstants.ACCESS_EXTERNAL_DTD property value is null");
        		System.out.println("Current3a xmlInputFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ xmlInputFactory.getProperty(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
*/				XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(
						new FileReader("src/main/resources/xxetest1.xml"));
				
				boolean reached = false;
				
				while(xmlStreamReader.hasNext()){
					xmlStreamReader.next();
				    if(xmlStreamReader.isStartElement()){
				        if (xmlStreamReader.getLocalName().equals("test")) {
				        	reached = true;
				        	assertFalse("The included file content was loaded from the system file.", 
				        			xmlStreamReader.getElementText().equals("SUCCESSFUL"));
				        }
				    }
				}
				assertTrue( reached );
	        } catch (XMLStreamException ex) {
        		System.out.println("OK: Java 1.8 with the XMLConstants.ACCESS_EXTERNAL_DTD Property set prevents XXE for XMLInputFactory "
    					+ " and returns the error: External Entity: Failed to read external document, because 'file' access is "
    					+ "not allowed due to restriction set by the accessExternalDTD property.");
/*        		System.out.println("Current3b xmlInputFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ xmlInputFactory.getProperty(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
	        	ex.printStackTrace();
*/	            assertTrue("Java 1.8 with the xmlInputFactory ACCESS_EXTERNAL_DTD properly set prevented XXE.", true);
	   		}
	}
	
	public void test3Java8JAXP1_5PropertiesSupportInXMLInputFactory() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		
		if (AppTest.javaVersion.startsWith("1.8")) {
			xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
//			xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // This one not supported for some reason
			assertTrue("Was able to set all XMLConstants.ACCESS_EXTERNAL_nnnn properties for XMLInputFactory using Java 8", true);
		}
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFile() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileReader("src/main/resources/xxetest2.xml"));
		
		boolean reached = false;
		
		while(xmlStreamReader.hasNext()){
			xmlStreamReader.next();
		    if(xmlStreamReader.isStartElement()){
		        if (xmlStreamReader.getLocalName().equals("test")) {
		        	reached = true;
		        	assertTrue("The expected text was not loaded from the network file.", xmlStreamReader.getElementText().equals("SUCCESSFUL"));
		        	//System.out.println(xmlStreamReader.getElementText().equals("SUCCESSFUL"));
		        	//System.out.println(xmlStreamReader.getElementText());
		        }
		    }
		}
		
		assertTrue( reached );
	}
	
	public void testLoadNonExistentNetworkFile() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		
		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileReader("src/main/resources/xxetest3.xml"));
	
			boolean reached = false;
			
			while(xmlStreamReader.hasNext()){
				xmlStreamReader.next();
			    if(xmlStreamReader.isStartElement()){
			        if (xmlStreamReader.getLocalName().equals("test")) {
			        	reached = true;
			        	assertFalse("The expected text should not have been loaded.", xmlStreamReader.getElementText().equals("SUCCESSFUL"));
			        	//System.out.println(xmlStreamReader.getElementText().equals("SUCCESSFUL"));
			        }
			    }
			}
			
			fail("The connection should not have succeeded. Should have thrown exception.");
		} catch (XMLStreamException e) {
			// ok
		}
	}
*/
}
