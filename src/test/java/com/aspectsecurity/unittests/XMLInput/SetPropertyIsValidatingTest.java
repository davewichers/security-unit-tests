package com.aspectsecurity.unittests.XMLInput;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import com.aspectsecurity.unittests.AppTest;
import junit.framework.TestCase;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileReader;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetPropertyIsValidatingTest extends TestCase {
	
	// Disabling validation doesn't prevent DTDs from being used/expanded.
	public void testValidationDisabled() throws Exception {
		
		try {
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty("javax.xml.stream.isValidating", "false");
			
			XMLStreamReader xmlStreamReader = 
				xmlInputFactory.createXMLStreamReader(new FileReader("src/main/resources/xxetest1.xml"));
			
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
			
			assertTrue( "Fell out of while loop for XMLInputFactory validation OFF test, without getting expected data", 
					reached );
        } catch (XMLStreamException ex) {
        	if (AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 prevents XXE for JAXBContexts because the default SAXParser it uses"
					+ " is set to: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property. "
					+ "So even though XML Validation is disabled, it still can parse a document that contains an external entity.");
        		System.out.println("Current javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else {
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the testValidationDisabled test");
			}
   		}

	}
	
	// Enabling validation doesn't prevent DTDs from being used/expanded, either.
	public void testValidationeNabled() throws Exception {
		
		try {
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty("javax.xml.stream.isValidating", "true");
			
			XMLStreamReader xmlStreamReader = 
				xmlInputFactory.createXMLStreamReader(new FileReader("src/main/resources/xxetest1.xml"));
			
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
			
			assertTrue( "Fell out of while loop for XMLInputFactory validation OFF test, without getting expected data", 
					reached );
        } catch (XMLStreamException ex) {
        	if (AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 prevents XXE for JAXBContexts because the default SAXParser it uses"
					+ " is set to: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property.");
        		System.out.println("Current javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else {
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the testValidationDisabled test");
			}
   		}
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFile() throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty("javax.xml.stream.isValidating", false);
		
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
		xmlInputFactory.setProperty("javax.xml.stream.isValidating", false);
		
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
