package com.aspectsecurity.unittests;

import java.security.CodeSource;
import java.text.MessageFormat;

/*
 * Copyright Aspect Security, Inc. All rights reserved.
 */

/**
 * Some XXE test cases
 * 
 * @author damodio
 * @author jpasski
 *
 */
public class App
{
	public static String getJaxpImplementationInfo(String componentName, Class componentClass) {
	    CodeSource source = componentClass.getProtectionDomain().getCodeSource();
	    Package p = componentClass.getPackage();
	    return MessageFormat.format(
	            "{0} implementation: {1} ({2}) version {3} ({4}) loaded from: {5}",
	            componentName,
	            componentClass.getName(),
	            p.getImplementationVendor(),
	            p.getSpecificationVersion(),
	            p.getImplementationVersion(),
	            source == null ? "Java_Runtime" : source.getLocation());
	}

    public static void main( String[] args )
    {
        System.out.println( "Execute the test cases with the [ mvn test ] command." );
        System.out.println( "Run tests and generate report with the [ mvn clean site surefire-report:report ] command.");
    }
}
