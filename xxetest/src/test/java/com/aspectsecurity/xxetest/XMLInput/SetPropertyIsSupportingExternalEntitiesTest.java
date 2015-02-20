package com.aspectsecurity.xxetest.XMLInput;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetPropertyIsSupportingExternalEntitiesTest extends TestCase {
	
	public void testLoadSystemFile() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
//        xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false); // This doesn't work
		
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(
				new FileReader("src/main/resources/xxetest1.xml"));
		
		boolean reached = false;
		
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
