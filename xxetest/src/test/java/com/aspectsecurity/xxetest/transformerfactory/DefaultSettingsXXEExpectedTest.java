package com.aspectsecurity.xxetest.transformerfactory;

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

import junit.framework.TestCase;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.aspectsecurity.xxetest.AppTest;

/*
 * DocumentBuilderFactory Test Cases
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultSettingsXXEExpectedTest extends TestCase {

	public void test1UnsafeTransformerFactory() throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(); // not passing in stylesheet

        try {
	        StreamSource input = new StreamSource(new FileInputStream("src/main/resources/xxetest1.xml"));
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        StreamResult output = new StreamResult(baos);
	        
	        transformer.transform(input, output);
	        String result = new String(baos.toByteArray());
	        //System.out.println("TransformerFactory result: " + result);
	
	    	if (AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8")) {
	    		fail("Setting the safe XXE processing system properties should prevent XXE with TransformerFactory in Java 8");
	    	}
	    	else assertTrue("The expected text was not loaded from the system file.", 
	   				result.contains("SUCCESSFUL"));
        } catch (TransformerException ex) {
        	if (AppTest.setSafeXMLProperties && AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 with the system settings properly set prevents XXE for TransformerFactory "
					+ " and returns the error: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property.");
        		System.out.println("Current1 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else {
        		System.out.println("Current2 javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
        		System.out.println("Current2 TransformerFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the TransformerFactory test1UnsafeTransformerFactory test");
			}
   		}
	}
	
	public void test2SafeTransformerFactory() throws Exception {
System.out.println("DRW: Entering TransformerFactory test 2"); 

		TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);  // Per documentation, this doesn't stop XXE
        
        Transformer transformer = factory.newTransformer(); // not passing in stylesheet

        // The following isn't safe (per above)
//      StreamSource input = new StreamSource(new FileInputStream("src/main/resources/xxetest1.xml"));
        
        // Apparently, the only way to make the transformer safe, is the wrap the source in a 'safe'
        // XML reader. For example:
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);  // This works
        
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
        	assertTrue("If DTDs are disabled, this exception could be thrown, which is OK.", true);
        }
System.out.println("DRW: Leaving TransformerFactory test 2"); 
	}
	

	public void test3ACCESS_EXTERNAL_DTDSupportInTransformerFactory() throws Exception {

System.out.println("DRW: Entering TransformerFactory test 3"); 
		TransformerFactory factory = TransformerFactory.newInstance();
		
		if (AppTest.javaVersion.startsWith("1.8"))
			try {
        		System.out.println("Current3a TransformerFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
				factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
				// TODO: Report to Xerces that this property value is null after setting it to an empty string, even 
				// though it still works. (Confirm this).
				if (factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) == null)
					System.out.println("DRW: XMLConstants.ACCESS_EXTERNAL_DTD property value is null");
        		System.out.println("Current3b TransformerFactory ACCESS_EXTERNAL_DTD property value: '" 
        				+ factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
        		
        		// Now try to see if its subject to XXE
                Transformer transformer = factory.newTransformer(); // not passing in stylesheet
                StreamSource input = new StreamSource(new FileInputStream("src/main/resources/xxetest1.xml"));
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                StreamResult output = new StreamResult(baos);
                
                transformer.transform(input, output);
                String result = new String(baos.toByteArray());
                //System.out.println("TransformerFactory result: " + result);

            	assertFalse("The expected text was loaded from the system file.", 
           			result.contains("SUCCESSFUL"));
            	
	        } catch (Exception ex) {
        		System.out.println("OK: Java 1.8 with the XMLConstants.ACCESS_EXTERNAL_DTD Attribute set prevents XXE for TransformerFactory "
    					+ " and returns the error: External Entity: Failed to read external document, because 'file' access is "
    					+ "not allowed due to restriction set by the accessExternalDTD attribute.");
//	        	ex.printStackTrace();
	            assertTrue("Java 1.8 with the TransformerFactory ACCESS_EXTERNAL_DTD properly set prevented XXE.", true);
	   		}
		finally {
System.out.println("DRW: Leaving TransformerFactory test 3");
		}
	}
	
	public void test4Java8JAXP1_5PropertiesSupportInTransformerFactory() throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		
		if (AppTest.javaVersion.startsWith("1.8")) {
			factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//			factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // This one not supported for some reason
			factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			assertTrue("Was able to set all XMLConstants.ACCESS_EXTERNAL_nnnn attributes for TransformerFactory using Java 8", true);
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
