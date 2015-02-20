package com.aspectsecurity.xxetest;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXHandler extends DefaultHandler {
	
		String testValue = "";
		 
		boolean test = false;
	 
		public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
			//System.out.println("Start Element :" + qName);
	 
			if (qName.equalsIgnoreCase("TEST")) {
				test = true;
			}
		}
	 
		public void endElement(String uri, String localName, String qName) throws SAXException {
			//System.out.println("End Element :" + qName);
		}
	 
		public void characters(char ch[], int start, int length) throws SAXException {
	 
			if (test) {
				this.testValue = new String(ch, start, length);
				//System.out.println("test : " + testValue);
				test = false;
			}
		}
		
		public String getTestValue() {
			return this.testValue;
		}
}
