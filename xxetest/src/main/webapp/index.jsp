<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Java XXE Tests</title>
	<style> .redText { color:red; } </style>
</head>
<body>

<h1>XXE Injection Vulnerability Tests for Java XML Parsers</h1>
<a href="index.jsp">XXE Injection Tests</a> | <a href="xmlquery.jsp">XPath and XQuery Injection Tests</a>
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
            out.println("Unsafe Tests: 21 <br> Safe Tests: 17");
        }
        else if (javaVersionMajor == 7 && javaVersionUpdate >= 51) {
            out.println("Unsafe Tests: 22 <br> Safe Tests: 16");
        }
        else if ((javaVersionMajor == 7 && javaVersionUpdate <= 45)) {
            out.println("Unsafe Tests: 23 <br> Safe Tests: 15");
        }
        else {
            out.println("These test cases have not been tested for the current Java version");
        }
    %>
</h3>

<p>Parsers (in alphabetical order by parser):</p>
<ul>
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/DocumentBuilder.html">javax.xml.parsers.<b>DocumentBuilder</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20DocumentBuilder&test=DocumentBuilder%3A%20Unsafe%20by%20Default%20Example&var=documentbuilderunsafedefault">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=%22Safe%22%20DocumentBuilder&test=DocumentBuilder%3A%20%22Safe%22%20when%20Disabling%20Entity%20Expansion%20Example%20%28FAILURE%29&var=documentbuildersafeexpansion">"Safe" when Disabling Entity Expansion Example <span class="redText">(FAILURE)</span></a></li>
            <li><a href="xmlview.jsp?title=Safe%20DocumentBuilder&test=DocumentBuilder%3A%20Safe%20when%20Disallowing%20DOCTYPE%20Declarations%20Example&var=documentbuildersafedoctype">Safe when Disallowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20DocumentBuilder&test=DocumentBuilder%3A%20Safe%20when%20Disabling%20External%20General%20Entities%20Example&var=documentbuildersafeexternal">Safe when Disabling External General Entities Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20DocumentBuilder&test=DocumentBuilder%3A%20Unsafe%20when%20Enabling%20External%20General%20Entities%20Example&var=documentbuilderunsafeexternal">Unsafe when Enabling External General Entities Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20DocumentBuilder&test=DocumentBuilder%3A%20Unsafe%20when%20Disabling%20Validation%20Example&var=documentbuilderunsafevalidationoff">Unsafe when Disabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20DocumentBuilder&test=DocumentBuilder%3A%20Unsafe%20when%20Enabling%20Validation%20Example&var=documentbuilderunsafevalidationon">Unsafe when Enabling Validation Example</a></li>
        </ul>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javaee/7/api/javax/xml/bind/JAXBContext.html">javax.xml.bind.<b>JAXBContext</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20JAXBContext&test=JAXBContext%3A%20Unsafe%20%28Safe%20in%20Java%201.8%20and%20up%29%20JAXBContext%20Unmarshaller%20from%20File%20Example&var=jaxbunsafefile">Unsafe (Safe in Java 1.8 and up) JAXBContext Unmarshaller from File Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20JAXBContext&test=JAXBContext%3A%20Unsafe%20JAXBContext%20Unmarshaller%20from%20Unsafe%20XMLInputFactory%20Example&var=jaxbunsafexmlinputfactory">Unsafe JAXBContext Unmarshaller from Unsafe XMLInputFactory Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20JAXBContext&test=JAXBContext%3A%20Safe%20JAXBContext%20Unmarshaller%20from%20Safe%20XMLInputFactory%20Example&var=jaxbsafexmlinputfactory">Safe JAXBContext Unmarshaller from Safe XMLInputFactory Example</a></li>
        </ul>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/SAXParser.html">javax.xml.parsers.<b>SAXParser</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20SAXParser&test=SAXParser%3A%20Unsafe%20by%20Default%20Example&var=saxunsafedefault">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20SAXParser&test=SAXParser%3A%20Safe%20when%20Disallowing%20DOCTYPE%20Declarations%20Example&var=saxsafedoctype">Safe when Disallowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20SAXParser&test=SAXParser%3A%20Unsafe%20when%20Allowing%20DOCTYPE%20Declarations%20Example&var=saxunsafedoctype">Unsafe when Allowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20SAXParser&test=SAXParser%3A%20Safe%20when%20Disabling%20External%20General%20and%20Parameter%20Entities%20Example&var=saxsafeexternal">Safe when Disabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20SAXParser&test=SAXParser%3A%20Unsafe%20when%20Enabling%20External%20General%20and%20Parameter%20Entities%20Example&var=saxunsafeexternal">Unsafe when Enabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20SAXParser&test=SAXParser%3A%20Unsafe%20when%20Disabling%20Validation%20Example&var=saxunsafevalidationoff">Unsafe when Disabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20SAXParser&test=SAXParser%3A%20Unsafe%20when%20Enabling%20Validation%20Example&var=saxunsafevalidationon">Unsafe when Enabling Validation Example</a></li>
        </ul>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/validation/Schema.html">javax.xml.parsers.<b>Schema</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20Schema&test=Schema%3A%20Unsafe%20by%20Default%20Example&var=schemaunsafe">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20Schema&test=Schema%3A%20Safe%20when%20Disallowing%20External%20DTDs%20and%20Schemas%20in%20Java%207u40%20and%20up%20Example&var=schemasafeaccess">Safe when Disallowing External DTDs and Schemas in Java 7u40 and up Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20Schema&test=Schema%3A%20Safe%20when%20Disallowing%20External%20DTDs%20and%20Schemas%20on%20the%20Validator%20in%20Java%207u40%20and%20up%20Example&var=schemasafeaccessvalidator">Safe when Disallowing External DTDs and Schemas on the Validator in Java 7u40 and up Example</a></li>
        </ul>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/transform/Transformer.html">javax.xml.transform.<b>Transformer</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20Transformer&test=Transformer%3A%20Unsafe%20by%20Default%20Example&var=transformerunsafedefault">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20Transformer&test=Transformer%3A%20Safe%20when%20Disallowing%20DTDs%20though%20a%20Safe%20XMLInputFactory%20Reader%20Example&var=transformersafedtd">Safe when Disallowing DTDs though a Safe XMLInputFactory Reader Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20Transformer&test=Transformer%3A%20Safe%20when%20Disallowing%20External%20DTDs%20in%20Java%207u40%20and%20up%20Example&var=transformersafeaccess">Safe when Disallowing External DTDs in Java 7u40 and up Example</a></li>
        </ul>
        <br />
	</li>

    <li><a href="https://docs.oracle.com/javase/7/docs/api/java/beans/XMLDecoder.html">java.beans.<b>XMLDecoder</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20XMLDecoder&test=XMLDecoder%3A%20Always%20Safe%20%28Always%20Unsafe%20in%20Java%201.7u45%20and%20earlier%29%20Example&var=xmldecoder">Always Safe (Always Unsafe in Java 1.7u45 and earlier) Example</a></li>
        </ul>
        <br />
    </li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLInputFactory.html">javax.xml.stream.<b>XMLInputFactory</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20XMLInputFactory&test=XMLInputFactory%3A%20Unsafe%20by%20Default%20Example&var=xmlinputunsafedefault">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20XMLInputFactory&test=XMLInputFactory%3A%20Safe%20when%20Disallowing%20External%20DTDs%20in%20Java%207u40%20and%20up%20Example&var=xmlinputsafeaccess">Safe when Disallowing External DTDs in Java 7u40 and up Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20XMLInputFactory&test=XMLInputFactory%3A%20Safe%20when%20Disallowing%20External%20Entities%20Example&var=xmlinputsafeexternal">Safe when Disallowing External Entities Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20XMLInputFactory&test=XMLInputFactory%3A%20Unsafe%20when%20Disabling%20Validation%20Example&var=xmlinputunsafevalidationoff">Unsafe when Disabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20XMLInputFactory&test=XMLInputFactory%3A%20Unsafe%20when%20Enabling%20Validation&var=xmlinputunsafevalidationon">Unsafe when Enabling Validation Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20XMLInputFactory&test=XMLInputFactory%3A%20Safe%20when%20Disabling%20DTD%20Support%20Example&var=xmlinputsafedtd">Safe when Disabling DTD Support Example</a></li>
        </ul>
        <br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/org/xml/sax/XMLReader.html">org.xml.sax.<b>XMLReader</b> (link to Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Unsafe%20XMLReader&test=XMLReader%3A%20Unsafe%20by%20Default%20Example&var=xmlreaderunsafedefault">Unsafe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20XMLReader&test=XMLReader%3A%20Safe%20when%20Disallowing%20DOCTYPE%20Declarations%20Example&var=xmlreadersafedoctype">Safe when Disallowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20XMLReader&test=XMLReader%3A%20Unsafe%20when%20Allowing%20DOCTYPE%20Declarations%20Example&var=xmlreaderunsafedoctype">Unsafe when Allowing DOCTYPE Declarations Example</a></li>
            <li><a href="xmlview.jsp?title=Safe%20XMLReader&test=XMLReader%3A%20Safe%20when%20Disabling%20External%20General%20and%20Parameter%20Entities%20Example&var=xmlreadersafeexternal">Safe when Disabling External General and Parameter Entities Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20XMLReader&test=XMLReader%3A%20Unsafe%20when%20Enabling%20External%20General%20and%20Parameter%20Entities%20Example&var=xmlreaderunsafeexternal">Unsafe when Enabling External General and Parameter Entities Example</a></li>
        </ul>
        <br />
	</li>

	<li><a href="http://www.xom.nu/apidocs/">nu.xom.Document (link to <b>XOM</b> Javadoc)</a>
        <ul>
            <li><a href="xmlview.jsp?title=Safe%20XOM&test=XOM%3A%20Safe%20by%20Default%20Example&var=xomsafe">Safe by Default Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20XOM&test=XOM%3A%20Unsafe%20when%20using%20an%20InputStream%20Example&var=xomunsafeinputstream">Unsafe when using an InputStream Example</a></li>
            <li><a href="xmlview.jsp?title=Unsafe%20XOM&test=XOM%3A%20Unsafe%20when%20Building%20from%20an%20Unsafe%20XMLReader%20Example&var=xomunsafexmlreader">Unsafe when Building from an Unsafe XMLReader Example</a></li>
        </ul>
        <br />
	</li>
</ul>

</body>
</html>