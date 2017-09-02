<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Java XPath Tests</title>
</head>
<body>

<h1>XPath Injection Vulnerability Tests for Java</h1>
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
		out.println("Java Version: " + Runtime.class.getPackage().getImplementationVersion() + "<br>");
		out.println("Unsafe Tests: tbd <br> Safe Tests: tbd");
	%>
</h3>

<p>XPath Implementations:</p>
<ul>
	<li><a href="https://xml.apache.org/xalan-j/apidocs/org/apache/xpath/package-summary.html">org.apache.xpath (link to <b>Apache Xalan-Java</b> Javadoc)</a>
		<ol start="46">
			<li><a href="xpathview.jsp?title=Unsafe%20Xalan&test=Xalan%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XPath%20Expression%20Example&var=xalanunsafeconcat">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Unsafe%20Xalan&test=Xalan%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XPath%20Expression%20Example&var=xalanunsafeplaceholder">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20Xalan&test=Xalan%3A%20Safe%20when%20Whitelisting%20on%20XPath%20Expression%20Example&var=xalansafelist">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20Xalan&test=Xalan%3A%20Safe%20when%20Escaping%20Apostrophes%20on%20XPath%20Expression%20Example&var=xalansafeescape">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="https://dom4j.github.io/javadoc/2.0.1/">org.dom4j (link to <b>DOM4J</b> Javadoc)</a>
		<ol start="50">
			<li><a href="xpathview.jsp?title=Unsafe%20DOM4J&test=DOM4J%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XPath%20Expression%20Example&var=dom4junsafeconcat">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Unsafe%20DOM4J&test=DOM4J%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XPath%20Expression%20Example&var=dom4junsafeplaceholder">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20DOM4J&test=DOM4J%3A%20Safe%20when%20Whitelisting%20on%20XPath%20Expression%20Example&var=dom4jsafelist">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20DOM4J&test=DOM4J%3A%20Safe%20when%20Escaping%20Apostrophes%20on%20XPath%20Expression%20Example&var=dom4jsafeescape">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/xpath/package-summary.html">javax.xml.xpath (link to <b>Java XPath API</b> Javadoc)</a>
		<ol start="54">
			<li><a href="xpathview.jsp?title=Unsafe%20XPath&test=Java%20XPath%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XPathExpression%20Example&var=xpathunsafeconcat">Unsafe when Using String Concatenation on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=Unsafe%20XPath&test=Java%20XPath%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XPathExpression%20Example&var=xpathunsafeplaceholder">Unsafe when Using String Placeholders on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20XPath&test=Java%20XPath%3A%20Safe%20when%20Parameterizing%20on%20XPathExpression%20Example&var=xpathsafeparam">Safe when Parameterizing on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20XPath&test=Java%20XPath%3A%20Safe%20when%20Whitelisting%20on%20XPathExpression%20Example&var=xpathsafelist">Safe when Whitelisting on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20XPath&test=Java%20XPath%3A%20Safe%20when%20Escaping%20Apostrophes%20on%20XPathExpression%20Example&var=xpathsafeescape">Safe when Escaping Apostrophes on XPathExpression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/package-summary.html">net.sf.saxon.s9api (link to <b>Saxonica Saxon9</b> Javadoc)</a>
		<ol start="59">
			<li><a href="xpathview.jsp?title=Unsafe%20Saxon&test=Saxon%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XPath%20Expression%20Example&var=saxonunsafeconcat">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Unsafe%20Saxon&test=Saxon%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XPath%20Expression%20Example&var=saxonunsafeplaceholder">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20Saxon&test=Saxon%3A%20Safe%20when%20Parameterizing%20on%20XPath%20Expression%20Example&var=saxonsafeparam">Safe when Parameterizing on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20Saxon&test=Saxon%3A%20Safe%20when%20Whitelisting%20on%20XPath%20Expression%20Example&var=saxonsafelist">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20Saxon&test=Saxon%3A%20Safe%20when%20Escaping%20Apostrophes%20on%20XPath%20Expression%20Example&var=saxonsafeescape">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="http://vtd-xml.sourceforge.net/javadoc/com/ximpleware/package-summary.html">com.ximpleware (link to <b>Ximpleware VTD-XML</b> Javadoc)</a>
		<ol start="64">
			<li><a href="xpathview.jsp?title=Unsafe%20VTD-XML&test=VTD-XML%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XPath%20Expression%20Example&var=vtdunsafeconcat">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Unsafe%20VTD-XML&test=VTD-XML%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XPath%20Expression%20Example&var=vtdunsafeplaceholder">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Unsafe%20VTD-XML&test=VTD-XML%3A%20Unsafe%20when%20Parameterizing%20on%20XPath%20Expression%20Example&var=vtdunsafeparam">Unsafe when Parameterizing on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20VTD-XML&test=VTD-XML%3A%20Safe%20when%20Whitelisting%20on%20XPath%20Expression%20Example&var=vtdsafelist">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=Safe%20VTD-XML&test=VTD-XML%3A%20Safe%20when%20Escaping%20Apostrophes%20on%20XPath%20Expression%20Example&var=vtdsafeescape">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>
</ul>

</body>
</html>