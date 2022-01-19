package com.aspectsecurity.unittestsweb.xxetestcases;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** Simple Handler that extracts the contents of any element named {@code <test>}. */
public class TestHandler extends DefaultHandler {
    private final StringBuilder testData;
    boolean inTest;

    public TestHandler() {
        this.testData = new StringBuilder();
        inTest = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("test".equals(qName) || "test".equals(localName)) {
            inTest = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("test".equals(qName) || "test".equals(localName)) {
            inTest = false;
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (inTest) {
            testData.append(ch, start, length);
        }
        super.characters(ch, start, length);
    }

    public String getTestData() {
        return testData.toString();
    }
}

