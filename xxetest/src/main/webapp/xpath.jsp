<%@ page import="org.owasp.encoder.Encode" %>
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
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe Xalan") %>&test=<%= Encode.forUriComponent("Xalan: Unsafe when Using String Concatenation on XPath Expression Example") %>&var=<%= Encode.forUriComponent("xalanunsafeconcat") %>">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe Xalan") %>&test=<%= Encode.forUriComponent("Xalan: Unsafe when Using String Placeholders on XPath Expression Example") %>&var=<%= Encode.forUriComponent("xalanunsafeplaceholder") %>">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe Xalan") %>&test=<%= Encode.forUriComponent("Xalan: Safe when Whitelisting on XPath Expression Example") %>&var=<%= Encode.forUriComponent("xalansafelist") %>">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe Xalan") %>&test=<%= Encode.forUriComponent("Xalan: Safe when Escaping Apostrophes on XPath Expression Example") %>&var=<%= Encode.forUriComponent("xalansafeescape") %>">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="https://dom4j.github.io/javadoc/2.0.1/">org.dom4j (link to <b>DOM4J</b> Javadoc)</a>
		<ol start="50">
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe DOM4J") %>&test=<%= Encode.forUriComponent("DOM4J: Unsafe when Using String Concatenation on XPath Expression Example") %>&var=<%= Encode.forUriComponent("dom4junsafeconcat") %>">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe DOM4J") %>&test=<%= Encode.forUriComponent("DOM4J: Unsafe when Using String Placeholders on XPath Expression Example") %>&var=<%= Encode.forUriComponent("dom4junsafeplaceholder") %>">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe DOM4J") %>&test=<%= Encode.forUriComponent("DOM4J: Safe when Whitelisting on XPath Expression Example") %>&var=<%= Encode.forUriComponent("dom4jsafelist") %>">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe DOM4J") %>&test=<%= Encode.forUriComponent("DOM4J: Safe when Escaping Apostrophes on XPath Expression Example") %>&var=<%= Encode.forUriComponent("dom4jsafeescape") %>">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/xpath/package-summary.html">javax.xml.xpath (link to <b>Java XPath API</b> Javadoc)</a>
		<ol start="54">
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe XPath") %>&test=<%= Encode.forUriComponent("Java XPath: Unsafe when Using String Concatenation on XPathExpression Example") %>&var=<%= Encode.forUriComponent("xpathunsafeconcat") %>">Unsafe when Using String Concatenation on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe XPath") %>&test=<%= Encode.forUriComponent("Java XPath: Unsafe when Using String Placeholders on XPathExpression Example") %>&var=<%= Encode.forUriComponent("xpathunsafeplaceholder") %>">Unsafe when Using String Placeholders on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe XPath") %>&test=<%= Encode.forUriComponent("Java XPath: Safe when Parameterizing on XPathExpression Example") %>&var=<%= Encode.forUriComponent("xpathsafeparam") %>">Safe when Parameterizing on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe XPath") %>&test=<%= Encode.forUriComponent("Java XPath: Safe when Whitelisting on XPathExpression Example") %>&var=<%= Encode.forUriComponent("xpathsafelist") %>">Safe when Whitelisting on XPathExpression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe XPath") %>&test=<%= Encode.forUriComponent("Java XPath: Safe when Escaping Apostrophes on XPathExpression Example") %>&var=<%= Encode.forUriComponent("xpathsafeescape") %>">Safe when Escaping Apostrophes on XPathExpression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/package-summary.html">net.sf.saxon.s9api (link to <b>Saxonica Saxon9</b> Javadoc)</a>
		<ol start="59">
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Unsafe when Using String Concatenation on XPath Expression Example") %>&var=<%= Encode.forUriComponent("saxonunsafeconcat") %>">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Unsafe when Using String Placeholders on XPath Expression Example") %>&var=<%= Encode.forUriComponent("saxonunsafeplaceholder") %>">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Safe when Parameterizing on XPath Expression Example") %>&var=<%= Encode.forUriComponent("saxonsafeparam") %>">Safe when Parameterizing on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Safe when Whitelisting on XPath Expression Example") %>&var=<%= Encode.forUriComponent("saxonsafelist") %>">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Safe when Escaping Apostrophes on XPath Expression Example") %>&var=<%= Encode.forUriComponent("saxonsafeescape") %>">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>

	<li><a href="http://vtd-xml.sourceforge.net/javadoc/com/ximpleware/package-summary.html">com.ximpleware (link to <b>Ximpleware VTD-XML</b> Javadoc)</a>
		<ol start="64">
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe VTD-XML") %>&test=<%= Encode.forUriComponent("VTD-XML: Unsafe when Using String Concatenation on XPath Expression Example") %>&var=<%= Encode.forUriComponent("vtdunsafeconcat") %>">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe VTD-XML") %>&test=<%= Encode.forUriComponent("VTD-XML: Unsafe when Using String Placeholders on XPath Expression Example") %>&var=<%= Encode.forUriComponent("vtdunsafeplaceholder") %>">Unsafe when Using String Placeholders on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Unsafe VTD-XML") %>&test=<%= Encode.forUriComponent("VTD-XML: Unsafe when Parameterizing on XPath Expression Example") %>&var=<%= Encode.forUriComponent("vtdunsafeparam") %>">Unsafe when Parameterizing on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe VTD-XML") %>&test=<%= Encode.forUriComponent("VTD-XML: Safe when Whitelisting on XPath Expression Example") %>&var=<%= Encode.forUriComponent("vtdsafelist") %>">Safe when Whitelisting on XPath Expression Example</a></li>
			<li><a href="xpathview.jsp?title=<%= Encode.forUriComponent("Safe VTD-XML") %>&test=<%= Encode.forUriComponent("VTD-XML: Safe when Escaping Apostrophes on XPath Expression Example") %>&var=<%= Encode.forUriComponent("vtdsafeescape") %>">Safe when Escaping Apostrophes on XPath Expression Example</a></li>
		</ol>
		<br />
	</li>
</ul>

</body>
</html>