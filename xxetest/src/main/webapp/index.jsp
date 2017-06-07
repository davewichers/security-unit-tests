<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.BufferedReader,java.io.FileReader,java.io.IOException,java.io.InputStreamReader"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Java XXE Tests</title>
</head>
<body>

<style> .redText { color:red; } </style>

<h1>XXE Vulnerability Tests for Java XML Parsers</h1>
<% 

//Anti-Caching Controls
response.setHeader("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1 controls 
response.setHeader("Pragma","no-cache"); //HTTP 1.0 controls
response.setDateHeader ("Expires", 0); //Prevents caching on proxy servers 

//Anti-Clickjacking Controls
response.setHeader("X-Frame-Options", "DENY");

out.println("<h3>Java Version: " + System.getProperty("java.version") + "<br>");
if (System.getProperty("java.version").startsWith("1.8"))
	out.println("Unsafe Tests: 19 <br> Safe Tests: 15</h3>");
else if (System.getProperty("java.version").startsWith("1.7"))
	out.println("Unsafe Tests: 22 <br> Safe Tests: 12</h3>");
else
	out.println("These test cases have not been tested for the current Java version</h3>");
%>

<p>Parsers (in alphabetical order by parser):</p>
<ul>
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/DocumentBuilder.html">javax.xml.parsers.<b>DocumentBuilder (link to JavaDoc)</b></a>
	<ul>
		<li><a href="documentbuilderunsafedefault.jsp">Unsafe by Default Example</a></li>
		<li><a href="documentbuildersafeexpansion.jsp">"Safe" when Disabling Entity Expansion Example <span class="redText">(FAILURE)</span></a></li>
		<li><a href="documentbuildersafedoctype.jsp">Safe when Disallowing DOCTYPE Declarations Example</a></li>
		<li><a href="documentbuildersafeexternal.jsp">Safe when Disabling External General Entities Example</a></li>
		<li><a href="documentbuilderunsafeexternal.jsp">Unsafe when Enabling External General Entities Example</a></li>
		<li><a href="documentbuilderunsafevalidationoff.jsp">Unsafe when Disabling Validation Example</a></li>
		<li><a href="documentbuilderunsafevalidationon.jsp">Unsafe when Enabling Validation Example</a></li>
	</ul>
	</li>
	
	<li><a href="https://docs.oracle.com/javaee/7/api/javax/xml/bind/JAXBContext.html">javax.xml.bind.<b>JAXBContext (link to JavaDoc)</b></a>
	<ul>
		<li><a href="jaxbunsafefile.jsp">Unsafe JAXBContext Unmarshaller from File Example (Safe in Java 1.8)</a></li>
		<li><a href="jaxbunsafexmlinputfactory.jsp">Unsafe JAXBContext Unmarshaller from Unsafe XMLInputFactory Example</a></li>
		<li><a href="jaxbsafexmlinputfactory.jsp">Safe JAXBContext Unmarshaller from Safe XMLInputFactory Example</a></li>
	</ul>
	</li>
	
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/SAXParser.html">javax.xml.parsers.<b>SAXParser (link to JavaDoc)</b></a>
	<ul>
		<li><a href="saxunsafedefault.jsp">Unsafe by Default Example</a></li>
		<li><a href="saxsafedoctype.jsp">Safe when Disallowing DOCTYPE Declarations Example</a></li>
		<li><a href="saxunsafedoctype.jsp">Unsafe when Allowing DOCTYPE Declarations Example</a></li>
		<li><a href="saxsafeexternal.jsp">Safe when Disabling External General Entities Example</a></li>
		<li><a href="saxunsafeexternal.jsp">Unsafe when Enabling External General Entities Example</a></li>
		<li><a href="saxunsafevalidationoff.jsp">Unsafe when Disabling Validation Example</a></li>
		<li><a href="saxunsafevalidationon.jsp">Unsafe when Enabling Validation Example</a></li>
	</ul>
	</li>
	
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/validation/Schema.html">javax.xml.parsers.<b>Schema (link to JavaDoc)</b></a>
	<ul>
		<li><a href="schemaunsafe.jsp">Unsafe by Default Example</a></li>
		<li><a href="schemasafe.jsp">Safe when Disallowing External DTDs and Schemas Example</a></li>
		<li><a href="schemasafevalidator.jsp">Safe when Disallowing External DTDs and Schemas on the Validator Example</a></li>
	</ul>
	</li>
	
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/transform/Transformer.html">javax.xml.transform.<b>Transformer (link to JavaDoc)</b></a>
	<ul>
		<li><a href="transformerunsafedefault.jsp">Unsafe by Default Example</a></li>
		<li><a href="transformersafedtd.jsp">Safe when Disallowing DTDs though a Safe XMLInputFactory Reader Example</a></li>
		<li><a href="transformersafeaccess.jsp">Safe when Disallowing External DTDs in Java 1.8 Example</a></li>
	</ul>
	</li>
	
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLInputFactory.html">javax.xml.stream.<b>XMLInputFactory (link to JavaDoc)</b></a>
	<ul>
		<li><a href="xmlinputunsafedefault.jsp">Unsafe by Default Example</a></li>
		<li><a href="xmlinputsafeaccess.jsp">Safe when Disallowing External DTDs in Java 1.8 Example</a></li>
		<li><a href="xmlinputsafeexternal.jsp">Safe when Disallowing External Entities Example</a></li>
		<li><a href="xmlinputunsafevalidationoff.jsp">Unsafe when Disabling Validation Example</a></li>
		<li><a href="xmlinputunsafevalidationon.jsp">Unsafe when Enabling Validation Example</a></li>
		<li><a href="xmlinputsafedtd.jsp">Safe when Disabling DTD Support Example</a></li>
	</ul>
	</li>
	
	<li><a href="https://docs.oracle.com/javase/7/docs/api/org/xml/sax/XMLReader.html">org.xml.sax.<b>XMLReader (link to JavaDoc)</b></a>
	<ul>
		<li><a href="xmlreaderunsafedefault.jsp">Unsafe by Default Example</a></li>
		<li><a href="xmlreadersafedoctype.jsp">Safe when Disallowing DOCTYPE Declarations Example</a></li>
		<li><a href="xmlreaderunsafedoctype.jsp">Unsafe when Allowing DOCTYPE Declarations Example</a></li>
		<li><a href="xmlreadersafeexternal.jsp">Safe when Disabling External General and Parameter Entities Example</a></li>
		<li><a href="xmlreaderunsafeexternal.jsp">Unsafe when Enabling External General and Parameter Entities Example</a></li>
	</ul>
	</li>
</ul>

</body>
</html>