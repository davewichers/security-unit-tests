<%@ page import="org.owasp.encoder.Encode" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Java XXE Tests</title>
	<style> .redText { color:red; } </style>
</head>
<body>

<h1>XXE Injection Vulnerability Tests for Java XML Parsers</h1>
<a href="index.jsp">XXE Injection Tests</a> | <a href="xpath.jsp">XPath Injection Tests</a> | <a href="xquery.jsp">XQuery Injection Tests</a>
<%
    //Anti-Caching Controls
    response.setHeader("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1 controls
    response.setHeader("Pragma","no-cache"); //HTTP 1.0 controls
    response.setDateHeader ("Expires", 0); //Prevents caching on proxy servers

    //Anti-Clickjacking Controls
    response.setHeader("X-Frame-Options", "DENY");
%>

<h3>
    <%
        final int javaVersionMajor = Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(2, 3));
        int javaVersionUpdate;
        if (Runtime.class.getPackage().getImplementationVersion().length() > 5) {
            javaVersionUpdate = Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(6));
        }
        else {
            javaVersionUpdate = 0;
        }

        out.println("Java Version: " + Runtime.class.getPackage().getImplementationVersion() + "<br>");
        if (javaVersionMajor >= 8) {
            out.println("Unsafe Tests: 29 <br> Safe Tests: 22");
        }
        else if (javaVersionMajor == 7 && javaVersionUpdate >= 51) {
            out.println("Unsafe Tests: 30 <br> Safe Tests: 21");
        }
        else if ((javaVersionMajor == 7 && javaVersionUpdate <= 45)) {
            out.println("Unsafe Tests: 31 <br> Safe Tests: 20");
        }
        else {
            out.println("These test cases have not been tested for the current Java version");
        }
    %>
</h3>

<% int testCount = 1;   // for numbering all tests %>

<p>Parsers (in alphabetical order by parser):</p>
<ul>
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/DocumentBuilder.html">javax.xml.parsers.<b>DocumentBuilder</b> (link to Javadoc)</a>
        <ol>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe DocumentBuilder") %>&test=<%= Encode.forUriComponent("DocumentBuilder: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("documentbuilderunsafedefault") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("\"Safe\" DocumentBuilder") %>&test=<%= Encode.forUriComponent("DocumentBuilder: \"Safe\" when Disabling Entity Expansion Example (FAILURE)") %>&var=<%= Encode.forUriComponent("documentbuildersafeexpand") %>">"Safe" when Disabling Entity Expansion Example <span class="redText">(FAILURE)</span></a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe DocumentBuilder") %>&test=<%= Encode.forUriComponent("DocumentBuilder: Safe when Disallowing DOCTYPE Declarations Example") %>&var=<%= Encode.forUriComponent("documentbuildersafedoctype") %>">Safe when Disallowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe DocumentBuilder") %>&test=<%= Encode.forUriComponent("DocumentBuilder: Safe when Disabling External General Entities Example") %>&var=<%= Encode.forUriComponent("documentbuildersafeexternal") %>">Safe when Disabling External General Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe DocumentBuilder") %>&test=<%= Encode.forUriComponent("DocumentBuilder: Unsafe when Enabling External General Entities Example") %>&var=<%= Encode.forUriComponent("documentbuilderunsafeexternal") %>">Unsafe when Enabling External General Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe DocumentBuilder") %>&test=<%= Encode.forUriComponent("DocumentBuilder: Unsafe when Disabling Validation Example") %>&var=<%= Encode.forUriComponent("documentbuilderunsafevalidationoff") %>">Unsafe when Disabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe DocumentBuilder") %>&test=<%= Encode.forUriComponent("DocumentBuilder: Unsafe when Enabling Validation Example") %>&var=<%= Encode.forUriComponent("documentbuilderunsafevalidationon") %>">Unsafe when Enabling Validation Example</a></li>
        </ol>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javaee/7/api/javax/xml/bind/JAXBContext.html">javax.xml.bind.<b>JAXBContext</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 7) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe JAXBContext") %>&test=<%= Encode.forUriComponent("JAXBContext: Unsafe (Safe in Java 1.8 and up) JAXBContext Unmarshaller from File Example") %>&var=<%= Encode.forUriComponent("jaxbunsafefile") %>">Unsafe (Safe in Java 1.8 and up) JAXBContext Unmarshaller from File Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe JAXBContext") %>&test=<%= Encode.forUriComponent("JAXBContext: Unsafe JAXBContext Unmarshaller from Unsafe XMLInputFactory Example") %>&var=<%= Encode.forUriComponent("jaxbunsafexmlinputfactory") %>">Unsafe JAXBContext Unmarshaller from Unsafe XMLInputFactory Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe JAXBContext") %>&test=<%= Encode.forUriComponent("JAXBContext: Safe JAXBContext Unmarshaller from Safe XMLInputFactory Example") %>&var=<%= Encode.forUriComponent("jaxbsafexmlinputfactory") %>">Safe JAXBContext Unmarshaller from Safe XMLInputFactory Example</a></li>
        </ol>
        <br />
	</li>

    <li><a href="http://www.jdom.org/docs/apidocs/org/jdom2/input/SAXBuilder.html">org.jdom2.input.<b>SAXBuilder</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 3) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXBuilder") %>&test=<%= Encode.forUriComponent("SAXBuilder: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("saxbuilderunsafedefault") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe SAXBuilder") %>&test=<%= Encode.forUriComponent("SAXBuilder: Safe when Disallowing DOCTYPE Declarations Example") %>&var=<%= Encode.forUriComponent("saxbuildersafedoctype") %>">Safe when Disallowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXBuilder") %>&test=<%= Encode.forUriComponent("SAXBuilder: Unsafe when Allowing DOCTYPE Declarations Example") %>&var=<%= Encode.forUriComponent("saxbuilderunsafedoctype") %>">Unsafe when Allowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXBuilder") %>&test=<%= Encode.forUriComponent("SAXBuilder: Unsafe when Disabling External General and Parameter Entities Example") %>&var=<%= Encode.forUriComponent("saxbuilderunsafeexternaloff") %>">Unsafe when Disabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXBuilder") %>&test=<%= Encode.forUriComponent("SAXBuilder: Unsafe when Enabling External General and Parameter Entities Example") %>&var=<%= Encode.forUriComponent("saxbuilderunsafeexternalon") %>">Unsafe when Enabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe SAXBuilder") %>&test=<%= Encode.forUriComponent("SAXBuilder: Safe when Disabling Entity Expansion Example") %>&var=<%= Encode.forUriComponent("saxbuildersafeexpand") %>">Safe when Disabling Entity Expansion Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXBuilder") %>&test=<%= Encode.forUriComponent("SAXBuilder: Unsafe when Enabling Entity Expansion Example") %>&var=<%= Encode.forUriComponent("saxbuilderunsafeexpand") %>">Unsafe when Enabling Entity Expansion Example</a></li>
        </ol>
        <br />
    </li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/SAXParser.html">javax.xml.parsers.<b>SAXParser</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 7) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXParser") %>&test=<%= Encode.forUriComponent("SAXParser: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("saxunsafedefault") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe SAXParser") %>&test=<%= Encode.forUriComponent("SAXParser: Safe when Disallowing DOCTYPE Declarations Example") %>&var=<%= Encode.forUriComponent("saxsafedoctype") %>">Safe when Disallowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXParser") %>&test=<%= Encode.forUriComponent("SAXParser: Unsafe when Allowing DOCTYPE Declarations Example") %>&var=<%= Encode.forUriComponent("saxunsafedoctype") %>">Unsafe when Allowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe SAXParser") %>&test=<%= Encode.forUriComponent("SAXParser: Safe when Disabling External General and Parameter Entities Example") %>&var=<%= Encode.forUriComponent("saxsafeexternal") %>">Safe when Disabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXParser") %>&test=<%= Encode.forUriComponent("SAXParser: Unsafe when Enabling External General and Parameter Entities Example") %>&var=<%= Encode.forUriComponent("saxunsafeexternal") %>">Unsafe when Enabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXParser") %>&test=<%= Encode.forUriComponent("SAXParser: Unsafe when Disabling Validation Example") %>&var=<%= Encode.forUriComponent("saxunsafevalidationoff") %>">Unsafe when Disabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe SAXParser") %>&test=<%= Encode.forUriComponent("SAXParser: Unsafe when Enabling Validation Example") %>&var=<%= Encode.forUriComponent("saxunsafevalidationon") %>">Unsafe when Enabling Validation Example</a></li>
        </ol>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/validation/Schema.html">javax.xml.parsers.<b>Schema</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 7) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe Schema") %>&test=<%= Encode.forUriComponent("Schema: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("schemaunsafe") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe Schema") %>&test=<%= Encode.forUriComponent("Schema: Safe when Disallowing External DTDs and Schemas in Java 7u40 and up Example") %>&var=<%= Encode.forUriComponent("schemasafeaccess") %>">Safe when Disallowing External DTDs and Schemas in Java 7u40 and up Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe Schema") %>&test=<%= Encode.forUriComponent("Schema: Safe when Disallowing External DTDs and Schemas on the Validator in Java 7u40 and up Example") %>&var=<%= Encode.forUriComponent("schemasafeaccessvalidator") %>">Safe when Disallowing External DTDs and Schemas on the Validator in Java 7u40 and up Example</a></li>
        </ol>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/transform/Transformer.html">javax.xml.transform.<b>Transformer</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 3) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe Transformer") %>&test=<%= Encode.forUriComponent("Transformer: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("transformerunsafedefault") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe Transformer") %>&test=<%= Encode.forUriComponent("Transformer: Safe when Disallowing DTDs though a Safe XMLInputFactory Reader Example") %>&var=<%= Encode.forUriComponent("transformersafedtd") %>">Safe when Disallowing DTDs though a Safe XMLInputFactory Reader Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe Transformer") %>&test=<%= Encode.forUriComponent("Transformer: Safe when Disallowing External DTDs in Java 7u40 and up Example") %>&var=<%= Encode.forUriComponent("transformersafeaccess") %>">Safe when Disallowing External DTDs in Java 7u40 and up Example</a></li>
        </ol>
        <br />
	</li>

    <li><a href="https://docs.oracle.com/javase/7/docs/api/java/beans/XMLDecoder.html">java.beans.<b>XMLDecoder</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 3) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLDecoder") %>&test=<%= Encode.forUriComponent("XMLDecoder: Always Safe (Always Unsafe in Java 1.7u45 and earlier) Example") %>&var=<%= Encode.forUriComponent("xmldecoder") %>">Always Safe (Always Unsafe in Java 1.7u45 and earlier) Example</a></li>
        </ol>
        <br />
    </li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLInputFactory.html">javax.xml.stream.<b>XMLInputFactory</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 1) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLInputFactory") %>&test=<%= Encode.forUriComponent("XMLInputFactory: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("xmlinputunsafedefault") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLInputFactory") %>&test=<%= Encode.forUriComponent("XMLInputFactory: Safe when Disallowing External DTDs in Java 7u40 and up Example") %>&var=<%= Encode.forUriComponent("xmlinputsafeaccess") %>">Safe when Disallowing External DTDs in Java 7u40 and up Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLInputFactory") %>&test=<%= Encode.forUriComponent("XMLInputFactory: Safe when Disallowing External Entities Example") %>&var=<%= Encode.forUriComponent("xmlinputsafeexternal") %>">Safe when Disallowing External Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLInputFactory") %>&test=<%= Encode.forUriComponent("XMLInputFactory: Unsafe when Disabling Validation Example") %>&var=<%= Encode.forUriComponent("xmlinputunsafevalidationoff") %>">Unsafe when Disabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLInputFactory") %>&test=<%= Encode.forUriComponent("XMLInputFactory: Unsafe when Enabling Validation") %>&var=<%= Encode.forUriComponent("xmlinputunsafevalidationon") %>">Unsafe when Enabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLInputFactory") %>&test=<%= Encode.forUriComponent("XMLInputFactory: Safe when Disabling DTD Support Example") %>&var=<%= Encode.forUriComponent("xmlinputsafedtd") %>">Safe when Disabling DTD Support Example</a></li>
        </ol>
        <br />
	</li>

    <li><a href="https://fasterxml.github.io/stax2-api/javadoc/4.0.0/org/codehaus/stax2/XMLInputFactory2.html">org.codehaus.stax2.<b>XMLInputFactory2</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 6) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLInputFactory2") %>&test=<%= Encode.forUriComponent("XMLInputFactory2: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("xmlinput2unsafedefault") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLInputFactory2") %>&test=<%= Encode.forUriComponent("XMLInputFactory2: Safe when Disallowing External DTDs in Java 7u40 and up Example") %>&var=<%= Encode.forUriComponent("xmlinput2safeaccess") %>">Safe when Disallowing External DTDs in Java 7u40 and up Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLInputFactory2") %>&test=<%= Encode.forUriComponent("XMLInputFactory2: Safe when Disallowing External Entities Example") %>&var=<%= Encode.forUriComponent("xmlinput2safeexternal") %>">Safe when Disallowing External Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLInputFactory2") %>&test=<%= Encode.forUriComponent("XMLInputFactory2: Unsafe when Disabling Validation Example") %>&var=<%= Encode.forUriComponent("xmlinput2unsafevalidationoff") %>">Unsafe when Disabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLInputFactory2") %>&test=<%= Encode.forUriComponent("XMLInputFactory2: Unsafe when Enabling Validation") %>&var=<%= Encode.forUriComponent("xmlinput2unsafevalidationon") %>">Unsafe when Enabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLInputFactory2") %>&test=<%= Encode.forUriComponent("XMLInputFactory2: Safe when Disabling DTD Support Example") %>&var=<%= Encode.forUriComponent("xmlinput2safedtd") %>">Safe when Disabling DTD Support Example</a></li>
        </ol>
        <br />
    </li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/org/xml/sax/XMLReader.html">org.xml.sax.<b>XMLReader</b> (link to Javadoc)</a>
        <ol start="<%= (testCount += 6) %>">
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLReader") %>&test=<%= Encode.forUriComponent("XMLReader: Unsafe by Default Example") %>&var=<%= Encode.forUriComponent("xmlreaderunsafedefault") %>">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLReader") %>&test=<%= Encode.forUriComponent("XMLReader: Safe when Disallowing DOCTYPE Declarations Example") %>&var=<%= Encode.forUriComponent("xmlreadersafedoctype") %>">Safe when Disallowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLReader") %>&test=<%= Encode.forUriComponent("XMLReader: Unsafe when Allowing DOCTYPE Declarations Example") %>&var=<%= Encode.forUriComponent("xmlreaderunsafedoctype") %>">Unsafe when Allowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Safe XMLReader") %>&test=<%= Encode.forUriComponent("XMLReader: Safe when Disabling External General and Parameter Entities Example") %>&var=<%= Encode.forUriComponent("xmlreadersafeexternal") %>">Safe when Disabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XMLReader") %>&test=<%= Encode.forUriComponent("XMLReader: Unsafe when Enabling External General and Parameter Entities Example") %>&var=<%= Encode.forUriComponent("xmlreaderunsafeexternal") %>">Unsafe when Enabling External General and Parameter Entities Example</a></li>
        </ol>
        <br />
	</li>

	<li><a href="http://www.xom.nu/apidocs/">nu.xom.Document (link to <b>XOM</b> Javadoc)</a>
        <ol start="<%= (testCount + 5) %>">
            <li><a href="xomview.jsp?title=<%= Encode.forUriComponent("Safe XOM") %>&test=<%= Encode.forUriComponent("XOM: Safe by Default Example") %>&var=<%= Encode.forUriComponent("xomsafe") %>">Safe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XOM") %>&test=<%= Encode.forUriComponent("XOM: Unsafe when using an InputStream Example") %>&var=<%= Encode.forUriComponent("xomunsafeinputstream") %>">Unsafe when using an InputStream Example</a></li>
            <li><a href="xmlview.jsp?title=<%= Encode.forUriComponent("Unsafe XOM") %>&test=<%= Encode.forUriComponent("XOM: Unsafe when Building from an Unsafe XMLReader Example") %>&var=<%= Encode.forUriComponent("xomunsafexmlreader") %>">Unsafe when Building from an Unsafe XMLReader Example</a></li>
        </ol>
        <br />
	</li>
</ul>

</body>
</html>