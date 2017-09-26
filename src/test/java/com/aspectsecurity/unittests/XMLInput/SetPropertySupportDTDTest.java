package com.aspectsecurity.unittests.XMLInput;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import junit.framework.TestCase;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileReader;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetPropertySupportDTDTest extends TestCase {
	
	public void testLoadSystemFile() throws Exception {
		boolean reached = false;

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		try {
			xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			
			try {
				XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(
						new FileReader("src/main/resources/xxetest1.xml"));
				
				while(xmlStreamReader.hasNext()){
					if (xmlStreamReader.hasNext())
					xmlStreamReader.next();
				    if(xmlStreamReader.isStartElement()){
				        if (xmlStreamReader.getLocalName().equals("test")) {
				        	reached = true;
				        	assertFalse("The expected text was not loaded from the system file.",
				        			xmlStreamReader.getElementText().equals("SUCCESSFUL"));
				        	//System.out.println(xmlStreamReader.getElementText());
				        }
				    }
				}
			} catch (XMLStreamException ex) {
				assertTrue("Included DTD was propertly rejected", true);
			}
		} catch (IllegalArgumentException ex) {
			fail("This xmlInputFactory doesn't support the property: " + XMLInputFactory.SUPPORT_DTD);
		}
		
		assertTrue( reached );

	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFile() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
		
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileReader("src/main/resources/xxetest2.xml"));
		
		boolean reached = false;
		
		while(xmlStreamReader.hasNext()){
			xmlStreamReader.next();
		    if(xmlStreamReader.isStartElement()){
		        if (xmlStreamReader.getLocalName().equals("test")) {
		        	reached = true;
		        	assertFalse("The expected text should not load from the network file.", xmlStreamReader.getElementText().equals("SUCCESSFUL"));
		        	
		        	//if (xmlStreamReader.getElementText().equals("SUCCESSFUL"))
		    		//	fail("The expected text should not load the system file.");
		        	
		        	//System.out.println(xmlStreamReader.getElementText());
		        }
		    }
		}
		
		assertTrue( reached );
	}
	
	public void testLoadNonExistentNetworkFile() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
		
//		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileReader("src/main/resources/xxetest3.xml"));
	
			boolean reached = false;
			
			while(xmlStreamReader.hasNext()){
				xmlStreamReader.next();
			    if(xmlStreamReader.isStartElement()){
			        if (xmlStreamReader.getLocalName().equals("test")) {
			        	reached = true;
			        	assertFalse("The expected text should not load from the network file.", xmlStreamReader.getElementText().equals("SUCCESSFUL"));
			        	
			        	//if (xmlStreamReader.getElementText().equals("SUCCESSFUL"))
			    		//	fail("The expected text should not load the network file.");
			        	
			        	//System.out.println(xmlStreamReader.getElementText());
			        }
			    }
			}
//		} catch (XMLStreamException e) {
//			//fail("Should not have attempted to make a network connection.");
//		}
	}
*/
}
