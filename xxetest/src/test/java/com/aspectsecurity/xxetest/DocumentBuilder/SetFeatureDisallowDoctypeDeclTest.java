package com.aspectsecurity.xxetest.DocumentBuilder;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetFeatureDisallowDoctypeDeclTest extends TestCase {
	
	public void testLoadSystemFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		try {
			Document doc = docBuilder.parse (new File("src/main/resources/xxetest1.xml"));
			assertTrue( doc != null );
			
			assertFalse("The expected text should not load the system file.", 
					doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
			
		} catch (NullPointerException e) {
			assertTrue("If DTDs are dissallowed, it could cause a null pointer exception, which is OK", true);
		} catch (SAXParseException s) {
			assertTrue("If DTDs are dissallowed, it could cause a SAXParseException, which is OK", true);
		}
	}

/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		try {
			Document doc = docBuilder.parse (new File("src/main/resources/xxetest2.xml"));
			assertTrue( doc != null );
			
			assertFalse("The expected text should not load from the network file.", doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
			
			if (doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"))
				fail("The expected text should not load the system file.");
		} catch (NullPointerException e) {
			// ok
		} catch (SAXParseException s) {
			// ok
		}
	}
	
	public void testLoadNonExistentNetworkFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = null;
		try {
			doc = docBuilder.parse (new File("src/main/resources/xxetest3.xml"));
		} catch (ConnectException e) {
			assertTrue(doc == null);
			fail("Should not have attempted to make a network connection.");
		} catch (SAXParseException s) {
			// ok
		}
		assertTrue(doc == null);
	}
*/
}
