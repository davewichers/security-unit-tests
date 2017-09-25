package com.aspectsecurity.unittests.DocumentBuilder;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import com.aspectsecurity.unittests.AppTest;
import junit.framework.TestCase;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetFeatureExternalGeneralEntitiesTest extends TestCase {
	
	// Disabling external entities should prevent XXE
	public void testExternalEntitiesDisabled() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false); // Make sure DTDs are allowed
		docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
// 		docBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = docBuilder.parse (new File("src/main/resources/xxetest1.xml"));
		assertTrue( doc != null );
		
		try {
			assertFalse("The expected text should not load the system file.", 
				doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
			
		} catch (NullPointerException e) {
			fail("The expected text should load the system file without causing an exception."); 
		}
	}
	
	// Enabling external entities should not prevent XXE
	public void testExternalEntitiesEnabled() throws Exception {
		// If the SafeXMLProperties are set using Java 1.8 while using a default DOMParser, XXE is prevented by default
		// even if these entity settings are enabled, because the DTDs themselves are prevented. If this is the case, don't
		// run this test.
    	if (!(AppTest.JAVARUNTIME.equals(AppTest.domLocation) && AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8"))) {	
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);
//			docBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
			
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			assertTrue( docBuilder != null );
			
			Document doc = docBuilder.parse (new File("src/main/resources/xxetest1.xml"));
			assertTrue( doc != null );
			
			try {
				assertTrue("The expected text should load the system file.", 
					doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
				
			} catch (NullPointerException e) {
				fail("The expected text should load the system file without causing an exception."); 
			}
    	}
    	else assertTrue("testExternalEntitiesEnabled is N/A in Java 1.8, with Safe XML properties set using default DOMBuilders", true);
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
	public void testLoadKnownNetworkFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = docBuilder.parse (new File("src/main/resources/xxetest2.xml"));
		assertTrue( doc != null );
		
		try {
			assertFalse("The expected text should not load from the network file.", doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
			
			if (doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"))
				fail("The expected text should not load the system file.");
		} catch (NullPointerException e) {
			// ok
		}
	}
	
	public void testLoadNonExistentNetworkFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		
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
