package com.aspectsecurity.unittests.SAXParser;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import com.aspectsecurity.unittests.AppTest;
import com.aspectsecurity.unittests.SAXHandler;
import junit.framework.TestCase;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetFeatureDisallowDoctypeDeclTest extends TestCase {
	
	public void testDisallowDoctypeDeclOn() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest1.xml", handler);
			
			assertFalse("The expected text should not load the system file.", handler.getTestValue().startsWith("SUCC"));
			
		} catch (SAXParseException s) {
			assertTrue("SAXParseException thrown because DTD is dissallowed. This is expected.", true);
		}
	}
	
	public void testDisallowDoctypeDeclOff() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest1.xml", handler);
			
			assertTrue("The expected text should load the system file.", handler.getTestValue().startsWith("SUCC"));
			
		} catch (SAXParseException s) {
        	if (AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 with the system settings properly set prevents XXE for DocumentBuilderFactory "
					+ " and returns the error: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property.");
        		System.out.println("Current1 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else
				fail("No exception should be thrown when disallow-doctype-decl is false");
		}
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFilePrevention() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest2.xml", handler);
			
			assertFalse("The expected text should not load from the network file.", handler.getTestValue().startsWith("SUCC"));
			
			if (handler.getTestValue().startsWith("SUCC"))
				fail("The expected text should not load the system file.");
		} catch (NullPointerException e) {
			// ok
		} catch (SAXParseException s) {
			// ok
		}
	}
	
	public void testLoadNonExistentNetworkFilePrevention() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest3.xml", handler);
		} catch (ConnectException e) {
			fail("Should not have attempted to make a network connection.");
		} catch (SAXParseException s) {
			// ok
		}

		if (handler.getTestValue().startsWith("SUCC"))
			fail("The expected text should not load the network file.");
	}
*/
}
