package com.aspectsecurity.xxetest.schemafactory;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;

/*
 * DocumentBuilderFactory Test Cases
 */
public class DefaultSettingsXXEExpectedTest extends TestCase {

	public void testXMLUsingSchemaFactory() throws Exception {
        SchemaFactory factory = 
        		SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // TODO: Need to define a schema file this factory can use.
//        Schema schema = factory.newSchema(new File("foo")); // not passing in stylesheet

        StreamSource input = new StreamSource(new FileInputStream("src/main/resources/xxetest1.xml"));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult output = new StreamResult(baos);
        
//        transformer.transform(input, output);
        String result = new String(baos.toByteArray());
        //System.out.println("TransformerFactory result: " + result);

//   		assertTrue("The expected text was not loaded from the system file.", 
//  				result.contains("SUCCESSFUL"));
	}
	
	public void testSafeXMLUsingSchemaFactory() throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);  // This is already on by default, or doesn't work
        //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");  // NOT SUPPORTED. Apparently this isn't a JAXP 1.5+ parser
        
        Transformer transformer = factory.newTransformer(); // not passing in stylesheet

        // The following isn't safe (per above)
//      StreamSource input = new StreamSource(new FileInputStream("src/main/resources/xxetest1.xml"));
        
        // Apparently, the only way to make the transformer safe, is the wrap the source in a 'safe'
        // XML reader like so:
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
//        inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", false); - TODO: Should test this too.
        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        try {
	        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(
	        		new FileReader("src/main/resources/xxetest1.xml"));
	        StAXSource input = new StAXSource(streamReader);
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        StreamResult output = new StreamResult(baos);
	        
	        transformer.transform(input, output);  // was input, output
	        String result = new String(baos.toByteArray());
	
	   		assertFalse("The expected text was loaded from the system file.", 
	   				result.contains("SUCCESSFUL"));
        } catch (TransformerException ex) {
        	// OK - this exception will be thrown if DTD's are disabled.
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
