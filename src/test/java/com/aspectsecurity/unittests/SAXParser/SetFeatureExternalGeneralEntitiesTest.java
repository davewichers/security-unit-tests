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
public class SetFeatureExternalGeneralEntitiesTest extends TestCase {
	
	// Disabling external general entities should prevent XXE
	public void testExternalEntitiesDisabled() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		saxParser.parse ("src/main/resources/xxetest1.xml", handler);
		
		assertFalse("The expected text should not load from the system file.", handler.getTestValue().startsWith("SUCC"));
	}
	
	// Enabling external general entities should allow XXE
	public void testExternalEntitiesEnabled() throws Exception {
		
		// If the SafeXMLProperties are set using Java 1.8 while using a default DOMParser, XXE is prevented by default
		// even if these entity settings are enabled, because the DTDs themselves are prevented. If this is the case, don't
		// run this test.
    	if (!(AppTest.JAVARUNTIME.equals(AppTest.saxLocation) && AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8"))) {	
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);
			saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
	
			SAXParser saxParser = saxParserFactory.newSAXParser();
			assertTrue( saxParser != null );
			
			SAXHandler handler = new SAXHandler();
			
			saxParser.parse ("src/main/resources/xxetest1.xml", handler);
			
			assertTrue("The expected text should load from the system file.", handler.getTestValue().startsWith("SUCC"));
    	}
    	else assertTrue("testExternalEntitiesEnabled is N/A in Java 1.8, with Safe XML properties set using default SAXParserFactory", true);
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFilePrevention() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		saxParser.parse ("src/main/resources/xxetest2.xml", handler);
		
		try {
			assertFalse("The expected text should not load from the network file.", handler.getTestValue().startsWith("SUCC"));
			
			if (handler.getTestValue().startsWith("SUCC"))
				fail("The expected text should not load the network file.");
		} catch (NullPointerException e) {
			// ok
		}
	}
	
	public void testLoadNonExistentNetworkFilePrevention() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		
		SAXParser saxParser = saxParserFactory.newSAXParser();
		assertTrue( saxParser != null );
		
		SAXHandler handler = new SAXHandler();
		
		try {
			saxParser.parse ("src/main/resources/xxetest3.xml", handler);
		} catch (ConnectException e) {
			fail("Should not have attempted to make a network connection.");
		}
		if (handler.getTestValue().startsWith("SUCC"))
			fail("The expected text should not load the network file.");
	}
*/
}
