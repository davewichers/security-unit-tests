package com.aspectsecurity.unittests.DocumentBuilder;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/*
 * DocumentBuilderFactory Test Cases
 */
public class SetExpandEntityReferencesTest extends TestCase {
	
	// This tests to see if disabling expansion of entity references works. The DTD is still allowed, but
	// the value of the entity shouldn't be placed into the resulting XML doc.
	
	// Given that this doesn't prevent XXE and we think it should, the body of this test is disabled
	public void testLoadSystemFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		System.out.println("Current DocumentBuilderFactory properties set to:");
		System.out.println("  Current isExpandEntityReferences value: '" 
			+ docBuilderFactory.isExpandEntityReferences() + "'");
		System.out.println("  Current isValidating value: '"
				+ docBuilderFactory.isValidating() + "'");
		System.out.println("  Current isCoalescing value: '"
				+ docBuilderFactory.isCoalescing() + "'");
		System.out.println("  Current isNamespaceAware value: '"
				+ docBuilderFactory.isNamespaceAware() + "'");
		System.out.println("  Current isXIncludeAware value: '"
				+ docBuilderFactory.isXIncludeAware() + "'");
		System.out.println("  Current http://xml.org/sax/features/external-general-entities feature value: '" 
				+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-general-entities") + "'");
		System.out.println("  Current http://xml.org/sax/features/external-parameter-entities feature value: '" 
				+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-parameter-entities") + "'");

		docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);
		docBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);

		// This doesn't seem to work, even though this value is indeed set to false
		// This is a Xerces implementation issue, not a Java Runtime issue
		// The Java Runtime version behaves the same
		docBuilderFactory.setExpandEntityReferences(false);
		
		System.out.println("After setting DocumentBuilderFactory to disable ExpandingEntityReferences:");
		System.out.println("  Current isExpandEntityReferences value: '" 
			+ docBuilderFactory.isExpandEntityReferences() + "'");
		System.out.println("  Current http://xml.org/sax/features/external-general-entities feature value: '" 
				+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-general-entities") + "'");
		System.out.println("  Current http://xml.org/sax/features/external-parameter-entities feature value: '" 
				+ docBuilderFactory.getFeature("http://xml.org/sax/features/external-parameter-entities") + "'");

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		//docBuilder.setEntityResolver(null);  // This doesn't prevent the entity expansion either.
		
		try {
		Document doc = docBuilder.parse(new File("src/main/resources/xxetest1.xml"));
		assertTrue( doc != null );
		
			try {
				String docContent = doc.getDocumentElement().getTextContent();
				System.out.println("[+] Doc content: " + docContent);
				assertFalse("The expected text should not load from the system file.", docContent.equals("SUCCESSFUL"));				

			} catch (NullPointerException e) {
				// ok
			}

		} catch (SAXParseException ex) {
			assertTrue( "The XML file should not parse because DTDs are not allowed", true);
		}
	}
	
/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.
	public void testLoadKnownNetworkFilePrevention() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setExpandEntityReferences(false);
		
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
		docBuilderFactory.setExpandEntityReferences(false);
		
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = null;
		try {
			doc = docBuilder.parse (new File("src/main/resources/xxetest3.xml"));
		} catch (ConnectException e) {
			assertTrue(doc == null);
			
			fail("Should not have attempted to make a network connection.");
		}
		assertTrue(doc == null);
	}
*/
}
