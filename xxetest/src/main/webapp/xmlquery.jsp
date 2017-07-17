<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Java XML Queries Tests</title>
</head>
<body>

<h1>XML Query Languages (XPath and XQuery) Injection Vulnerability Tests for Java</h1>
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
		out.println("Java Version: " + Runtime.class.getPackage().getImplementationVersion() + "<br>");
		if (System.getProperty("java.version").startsWith("1.8")) {
			out.println("Unsafe Tests: 22 <br> Safe Tests: 16");
		}
		else if (System.getProperty("java.version").startsWith("1.7")) {
			out.println("Unsafe Tests: 25 <br> Safe Tests: 13");
		}
		else {
			out.println("These test cases have not been tested for the current Java version");
		}
	%>
</h3>

<p>XPath Implementations:</p>
<ul>
	<li><a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/xpath/package-summary.html">javax.xml.xpath (link to <b>Java XPath API</b> Javadoc)</a>
		<ul>
			<li><a href="xmlqueryview.jsp?title=Unsafe%20XPath&test=Java%20XPath%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XPathExpression%20Example&var=xpathunsafeconcat">Unsafe when Using String Concatenation on XPathExpression Example</a></li>
			<li><a href="xmlqueryview.jsp?title=Unsafe%20XPath&test=Java%20XPath%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XPathExpression%20Example&var=xpathunsafeplaceholder">Unsafe when Using String Placeholders on XPathExpression Example</a></li>
			<li><a href="xmlqueryview.jsp?title=Safe%20XPath&test=Java%20XPath%3A%20Safe%20when%20Parameterizing%20on%20XPathExpression%20Example&var=xpathsafeparam">Safe when Parameterizing on XPathExpression Example</a></li>
			<li><a href="xmlqueryview.jsp?title=Safe%20XPath&test=Java%20XPath%3A%20Safe%20when%20Escaping%20Apostrophes%20on%20XPathExpression%20Example&var=xpathsafeescape">Safe when Escaping Apostrophes on XPathExpression Example</a></li>
		</ul>
		<br />
	</li>

	<li><a href="https://xml.apache.org/xalan-j/apidocs/org/apache/xpath/package-summary.html">org.apache.xpath (link to <b>Apache Xalan-Java</b> Javadoc)</a>
		<ul>
			<li><a href="xmlqueryview.jsp?title=Unsafe%20Xalan&test=Xalan%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XPath%20Expression%20Example&var=xalanunsafeconcat">Unsafe when Using String Concatenation on XPath Expression Example</a></li>
			<li><a href="xmlqueryview.jsp?title=Unsafe%20Xalan&test=Xalan%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XPath%20Expression%20Example&var=xalanunsafeplaceholder">Unsafe when Using String Placeholders on XPathExpression Example</a></li>
			<li><a href="xmlqueryview.jsp?title=Safe%20Xalan&test=Xalan%3A%20Safe%20when%20Escaping%20Apostrophes%20on%20XPath%20Expression%20Example&var=xalansafeescape">Safe when Escaping Apostrophes on XPathExpression Example</a></li>
		</ul>
		<br />
	</li>
</ul>

<p>XQuery Implementations:</p>
<ul>
	<li><a href="https://docs.oracle.com/database/121/JAXML/javax/xml/xquery/package-summary.html">javax.xml.xquery (link to <b>XQuery</b> Javadoc)</a>
		<ul>
			<li><a href="xmlqueryview.jsp?title=Unsafe%20XQuery&test=XQuery%3A%20Unsafe%20by%20Default%20Example&var=xqueryunsafedefault">Unsafe by Default Example</a></li>
		</ul>
		<br />
	</li>
</ul>

</body>
</html>