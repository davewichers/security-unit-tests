package com.aspectsecurity.xxetest.jaxb;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;

import junit.framework.TestCase;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.aspectsecurity.xxetest.AppTest;

/** 
  * This shows how to use JAXB to unmarshal an xml file
  * Then display the information from the content tree
  */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultSettingsXXEExpectedTest extends TestCase {

    public void test1JAXBContextUnmarshaller() throws Exception {

        JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
//        System.out.println("Underlying property value: XMLConstants.ACCESS_EXTERNAL_DTD: '"
//				+ unmarshaller.getProperty(XMLConstants.ACCESS_EXTERNAL_DTD) + "'");
        System.out.println(AppTest.getJaxpImplementationInfo("Unmarshaller", unmarshaller.getClass()));
//        System.out.println(AppTest.getJaxpImplementationInfo("Unmarshaller Listener", unmarshaller.getListener().getClass()));
        
        try {
	        Collection collection= (Collection)
	                 unmarshaller.unmarshal(new File( "src/main/resources/xxetestbook1.xml"));
        	
	        Collection.Books booksType = collection.getBooks();
	        List<BookType> bookList = booksType.getBook();
	
	        String discount = "";
	        for( int i = 0; i < bookList.size();i++ ) {
	        	BookType book =(BookType) bookList.get(i);           
	            discount = book.getPromotion().getDiscount().trim();
	            System.out.println("Book promotion default: " +  discount);
	            assertTrue("The expected text was not loaded from the system file.", discount.contains("SUCCESS"));
	        }
        } catch (UnmarshalException ex) {
        	if (AppTest.javaVersion.startsWith("1.8") && AppTest.JAVARUNTIME.equals(AppTest.saxLocation)) {
        		// apparently in Java 1.8 the saxparser used by JAXBContext by default prevent XXE, so its safe by default
	        	// However, I don't know which SAXParser this is.  Here is the error message:
	 /* [org.xml.sax.SAXParseException; systemId: file:/C:/CASTestCases/workspace/xxetest/src/main/resources/xxetestbook1.xml; 
	  * lineNumber: 24; columnNumber: 34; External Entity: Failed to read external document 'xxe.txt', because 'file' 
	  * access is not allowed due to restriction set by the accessExternalDTD property.]
	    at javax.xml.bind.helpers.AbstractUnmarshallerImpl.createUnmarshalException(Unknown Source)
        at com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl.createUnmarshalException(Unknown Source)
        at com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl.unmarshal0(Unknown Source)
        at com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl.unmarshal(Unknown Source)
        at javax.xml.bind.helpers.AbstractUnmarshallerImpl.unmarshal(Unknown Source)
        at javax.xml.bind.helpers.AbstractUnmarshallerImpl.unmarshal(Unknown Source)
	        	ex.printStackTrace();
*/        		assertTrue("JAXBContext in Java 1.8 uses an (unidentified) native saxparser that is safe by default", true);
        	}
        	else {
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the testJAXBContextUnmarshaller test");
        	}
        }
    }

    public void test2UnsafeJAXBContextUnmarshaller() throws Exception {

        JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty("javax.xml.stream.supportDTD", true);

        try {
	        Collection collection= (Collection)
	                 unmarshaller.unmarshal(factory.createXMLStreamReader(
	                		 new FileInputStream( "src/main/resources/xxetestbook1.xml")));
	
	        Collection.Books booksType = collection.getBooks();
	        List<BookType> bookList = booksType.getBook();
	
	        for( int i = 0; i < bookList.size();i++ ) {
	        	BookType book =(BookType) bookList.get(i);           
	            String discount = book.getPromotion().getDiscount().trim();
	            System.out.println("Book promotion unsafe: " +  discount);
	            assertTrue("The expected text was not loaded from the system file.", discount.contains("SUCCESS"));
	        }
        } catch (UnmarshalException ex) {
        	if (AppTest.javaVersion.startsWith("1.8")) {
        		System.out.println("OK: Java 1.8 prevents XXE for JAXBContexts because the default SAXParser it uses"
					+ " is set to: External Entity: Failed to read external document, because 'file' access is "
					+ "not allowed due to restriction set by the accessExternalDTD property.");
        		System.out.println("Current javax.xml.accessExternalDTD system property value: '" 
        				+ System.getProperty("javax.xml.accessExternalDTD") + "'");
	            assertTrue("Java 1.8 with the system settings properly set prevented XXE.", true);
			} else {
	        	ex.printStackTrace();
	        	fail("No exception should be thrown in the testUnsafeJAXBContextUnmarshaller test");
			}
        }
	}

    public void test3SafeJAXBContextUnmarshaller() throws Exception {

        JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty("javax.xml.stream.supportDTD", false);

        try {
	        Collection collection= (Collection)
	                 unmarshaller.unmarshal(factory.createXMLStreamReader(
	                		 new FileInputStream( "src/main/resources/xxetestbook1.xml")));
	
	        Collection.Books booksType = collection.getBooks();
	        List<BookType> bookList = booksType.getBook();
	
	        for( int i = 0; i < bookList.size();i++ ) {
	        	BookType book =(BookType) bookList.get(i);           
	            String discount = book.getPromotion().getDiscount().trim();
	            System.out.println("Book promotion Safe: " +  discount);
	            assertFalse("The expected text was loaded from the system file.", discount.contains("SUCCESS"));
	        }
        } catch (UnmarshalException ex) {
        	// ok - the parser should throw this exception if DTD's are disallowed
        	assertTrue("Unmarshaller should throw an exception since DTDs are dissallowed", true);
        }
    }


/*	// Tests commented out because xxetest2.xml and xxetest3.xml try to read a file from a web server, which we don't have set up.    
    public void testLoadKnownNetworkFile() throws Exception {

        JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        Collection collection= (Collection)
                 unmarshaller.unmarshal(new File( "src/main/resources/xxetestbook2.xml"));

        Collection.Books booksType = collection.getBooks();
        List bookList = booksType.getBook();

        for( int i = 0; i < bookList.size();i++ ) {
        	BookType book =(BookType) bookList.get(i);           
            String discount = book.getPromotion().getDiscount().trim();
            System.out.println("Book promotion: " +  discount);
            assertTrue("The expected text was loaded from the system file.", discount.contains("SUCCESS"));
        }
    }
    
    public void testLoadNonExistentNetworkFile() throws Exception {

        JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();


        try {
	        Collection collection= (Collection)
	                 unmarshaller.unmarshal(new File( "src/main/resources/xxetestbook3.xml"));

	        Collection.Books booksType = collection.getBooks();
	        List bookList = booksType.getBook();
	
	        for( int i = 0; i < bookList.size();i++ ) {
	        	BookType book =(BookType) bookList.get(i);           
	            String discount = book.getPromotion().getDiscount().trim();
	            System.out.println("Book promotion: " +  discount);
	            assertTrue("The expected text was loaded from the system file.", discount.contains("SUCCESS"));
	        }
        } catch (UnmarshalException e) {
        	e.printStackTrace();
			assertTrue(true);
        }
    }
*/
}
