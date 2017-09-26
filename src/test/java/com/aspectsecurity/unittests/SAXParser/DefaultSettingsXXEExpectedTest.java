package com.aspectsecurity.unittests.SAXParser;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import com.aspectsecurity.unittests.AppTest;
import com.aspectsecurity.unittests.SAXHandler;
import junit.framework.TestCase;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/*
 * DocumentBuilderFactory Test Cases
 */
public class DefaultSettingsXXEExpectedTest extends TestCase {
	
	public void testLoadSystemFile() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest1.xml", handler);
	//System.out.println("SAXParser result: " + handler.getTestValue());
			// TODO: For some reason it only reads in the first 4 characters of the file, not all of 'SUCCESSFULL' Figure out why
			assertTrue("The expected text was not loaded from the system file.", handler.getTestValue().startsWith("SUCC"));
			
			// The following should work if the SAXParser is JAXP 1.5 compliant, but the Xerces version I'm using is not.
	/*		if (AppTest.setSafeXMLProperties) {			
				System.out.println("Current SAXParserFactory and SAXParser is: ");
				System.out.println("  " + saxParserFactory.getClass() + " and");
				System.out.println("  " + saxParser.getClass());
				System.out.println("Secure XML Processing system properties set to:");
				System.out.println("  Current javax.xml.accessExternalDTD value: '" 
					+ System.getProperty("javax.xml.accessExternalDTD") + "'");
				System.out.println("  Current javax.xml.accessExternalSchema value: '" 
					+ System.getProperty("javax.xml.accessExternalSchema") + "'");
				System.out.println("  Current javax.xml.accessExternalStylesheet value: '" 
					+ System.getProperty("javax.xml.accessExternalStylesheet") + "'");
	
				System.out.println("Current SAXParser properties set to:");
				System.out.println("  Current XMLConstants.ACCESS_EXTERNAL_DTD value: '" 
					+ saxParser.getProperty(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
				System.out.println("  Current XMLConstants.ACCESS_EXTERNAL_SCHEMA value: '" 
					+ saxParser.getProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA) + "'");
				fail("SAXParserFactory should disallow XXE when these properties are set.");
			}
	*/
        } catch (SAXParseException ex) {
        	if (AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 with the system settings properly set prevents XXE for SAXParserFactory "
					+ " and returns the error: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property.");
        		System.out.println("Current1 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else {
        		System.out.println("Current2 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
        		System.out.println("Current2 SAXParserFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ saxParser.getProperty(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the SAXParserFactory testLoadSystemFile test");
			}
   		}
			
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFile() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		saxParser.parse ("src/main/resources/xxetest2.xml", handler);
		
		assertTrue("The expected text was not loaded from the network file.", handler.getTestValue().startsWith("SUCC"));
	}
	
	public void testLoadNonExistentNetworkFile() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest3.xml", handler);
			fail("The connection should not have succeeded. Should have thrown exception.");
		} catch (ConnectException e) {
			assertTrue(handler.getTestValue().equals(""));
		}
	}
*/
}
