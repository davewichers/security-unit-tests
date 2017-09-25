package com.aspectsecurity.unittests.DocumentBuilder;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import com.aspectsecurity.unittests.AppTest;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/*
 * DocumentBuilderFactory Test Cases
 */
public class DefaultSettingsXXEExpectedTest extends TestCase {
	
	public void testLoadSystemFile() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		try {
			Document doc = docBuilder.parse (new File("src/main/resources/xxetest1.xml"));
			assertTrue( doc != null );
			
			assertTrue("The expected text was not loaded from the system file.", 
					doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
	
			// The following should work if the SAXParser is JAXP 1.5 compliant, but the Xerces version I'm using is not.
	/*		if (AppTest.setSafeXMLProperties) {			
				System.out.println("Current DocumentBuilderFactory and DocumentBuilder is: ");
				System.out.println("  " + docBuilderFactory.getClass() + " and");
				System.out.println("  " + docBuilder.getClass());
				System.out.println("Secure XML Processing system properties set to:");
				System.out.println("  Current javax.xml.accessExternalDTD value: '" 
					+ System.getProperty("javax.xml.accessExternalDTD") + "'");
				System.out.println("  Current javax.xml.accessExternalSchema value: '" 
					+ System.getProperty("javax.xml.accessExternalSchema") + "'");
				System.out.println("  Current javax.xml.accessExternalStylesheet value: '" 
					+ System.getProperty("javax.xml.accessExternalStylesheet") + "'");
	
				System.out.println("Current DocumentBuilderFactory properties set to:");
				System.out.println("  Current isExpandEntityReferences value: '" 
					+ docBuilderFactory.isExpandEntityReferences() + "'");
				System.out.println("  Current isValidating value: '" 
						+ docBuilderFactory.isValidating() + "'");
				
				docBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "file, jar:file");
				
				System.out.println("  Current docBuilderFactory attribute javax.xml.accessExternalDTD value: '" 
					+ docBuilderFactory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
				System.out.println("  Current docBuilderFactory attribute javax.xml.accessExternalSchema value: '" 
					+ docBuilderFactory.getAttribute("javax.xml.accessExternalSchema") + "'");
				System.out.println("  Current docBuilderFactory attribute javax.xml.accessExternalStylesheet value: '" 
					+ docBuilderFactory.getAttribute("javax.xml.accessExternalStylesheet") + "'");			
				fail("DocumentBuilderFactory should disallow XXE when these properties are set.");
			}
*/
        } catch (SAXParseException ex) {
        	if (AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 with the system settings properly set prevents XXE for DocumentBuilderFactory "
					+ " and returns the error: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property.");
        		System.out.println("Current1 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else {
        		System.out.println("Current2 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
        		System.out.println("Current2 DocumentBuilderFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ docBuilderFactory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the DocumentBuilderFactory testLoadSystemFile test");
			}
   		}
	}

/*	// Test commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.
	public void testLoadKnownNetworkFile() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = docBuilder.parse (new File("src/main/resources/xxetest2.xml"));
		assertTrue( doc != null );
		
		assertTrue("The expected text was not loaded from the network file.", doc.getDocumentElement().getTextContent().equals("SUCCESSFUL"));
	}

	public void testLoadNonExistentNetworkFile() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		assertTrue( docBuilder != null );
		
		Document doc = null;
		try {
			doc = docBuilder.parse (new File("src/main/resources/xxetest3.xml"));
			fail("The connection should not have succeeded. Should have thrown exception.");
		} catch (ConnectException e) {
			assertTrue(doc == null);
		}
		assertTrue(doc == null);
	}
*/
}
