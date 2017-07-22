package com.aspectsecurity.xxetestweb;

import java.io.Serializable;

// Java Bean for use with XMLDecoder
public class XMLTestBean implements Serializable {

    private String element;

    public XMLTestBean() {}
    public XMLTestBean(final String element) {
        setElement(element);
    }

    public void setElement(final String element) {
        this.element = element;
    }
    public String getElement() {
        return this.element;
    }
}