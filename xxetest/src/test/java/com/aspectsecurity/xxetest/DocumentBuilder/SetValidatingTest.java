package com.aspectsecurity.xxetest.DocumentBuilder;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.aspectsecurity.xxetest.AppTest;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetValidatingTest extends TestCase {

  // Disabling validation doesn't prevent DTDs from being used/expanded.
	public void testValidationDisabled() throws Exception {
    	if (!(AppTest.JAVARUNTIME.equals(AppTest.domLocation) && AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8"))) {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setValidating(false);
			
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			assertTrue( docBuilder != null );
			
			Document doc = docBuilder.parse (new File("src/main/resources/xxetest1.xml"));
			assertTrue( doc != null );
			
			assertTrue("The expected text did not load from the system file.", 
					doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
    	}
	}

  // Enabling validation doesn't prevent DTDs from being used/expanded, either.
	public void testValidationEnabled() throws Exception {
    	if (!(AppTest.JAVARUNTIME.equals(AppTest.domLocation) && AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8"))) {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setValidating(true);
			
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			assertTrue( docBuilder != null );
			
			Document doc = docBuilder.parse (new File("src/main/resources/xxetest1.xml"));
			assertTrue( doc != null );
			
			assertTrue("The expected text did not load from the system file.", 
					doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
    	}
	}

/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.
	public void testLoadKnownNetworkFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setValidating(false);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = docBuilder.parse (new File("src/main/resources/xxetest2.xml"));
		assertTrue( doc != null );
		
		assertFalse("The expected text should not load from the network file.", doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
		
		if (doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"))
			fail("The expected text should not load from the network file.");
	}
	
	public void testLoadNonExistentNetworkFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setValidating(false);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = null;
		try {
			doc = docBuilder.parse (new File("src/main/resources/xxetest3.xml"));
		} catch (ConnectException e) {
			assertTrue(doc == null);
			
			fail("Should not have attempted to make a network connection.");
		}
	}
*/
}
