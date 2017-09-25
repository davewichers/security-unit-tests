package com.aspectsecurity.unittests.SAXParser;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import com.aspectsecurity.unittests.AppTest;
import com.aspectsecurity.unittests.SAXHandler;
import junit.framework.TestCase;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetValidatingTest extends TestCase {
	
    // Disabling validation doesn't prevent DTDs from being used/expanded.
	public void testValidationDisabled() throws Exception {
    	if (!(AppTest.JAVARUNTIME.equals(AppTest.saxLocation) && AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8"))) {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setValidating(false);
			
			SAXParser saxParser = saxParserFactory.newSAXParser();
			assertTrue( saxParser != null );
			
			SAXHandler handler = new SAXHandler();
			
			saxParser.parse ("src/main/resources/xxetest1.xml", handler);
			
			assertTrue("The expected text was not loaded from the system file.", handler.getTestValue().startsWith("SUCC"));
    	}
	}
	
	// Enabling validation doesn't prevent DTDs from being used/expanded, either.
	public void testValidationEnabled() throws Exception {
    	if (!(AppTest.JAVARUNTIME.equals(AppTest.saxLocation) && AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8"))) {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setValidating(true);
			
			SAXParser saxParser = saxParserFactory.newSAXParser();
			assertTrue( saxParser != null );
			
			SAXHandler handler = new SAXHandler();
			
			saxParser.parse ("src/main/resources/xxetest1.xml", handler);
			
			assertTrue("The expected text was not loaded from the system file.", handler.getTestValue().startsWith("SUCC"));
    	}
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFile() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(false);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		saxParser.parse ("src/main/resources/xxetest2.xml", handler);
		
		assertFalse("The expected text was loaded from the network file.", handler.getTestValue().startsWith("SUCC"));
		
		if (handler.getTestValue().startsWith("SUCC"))	
			fail("The expected text should not load the network file.");
	}
	
	public void testLoadNonExistentNetworkFile() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(false);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest3.xml", handler);
			//fail("The connection should not have succeeded. Should have thrown exception.");
		} catch (ConnectException e) {
			assertTrue(handler.getTestValue().equals(""));
			
			fail("Should not have attempted to make a network connection.");
		}
	}
*/
}
