<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Java XQuery Tests</title>
</head>
<body>

<h1>XQuery Injection Vulnerability Tests for Java</h1>
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

<p>XQuery Implementations:</p>
<ul>
	<li><a href="https://docs.oracle.com/database/121/JAXML/javax/xml/xquery/package-summary.html">javax.xml.xquery (link to <b>XQuery API for Java (XQJ)</b> Javadoc)</a>
		<ul>
			<li>Using <a href="http://www.saxonica.com/html/documentation9.6/javadoc/com/saxonica/xqj/SaxonXQDataSource.html">com.saxonica.xqj.SaxonXQDataSource (link to <b>Saxonica Saxon9</b> Javadoc)</a>
				<ul>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XQExpression%20Example&var=saxonunsafeconcat">Unsafe when Using String Concatenation on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XQExpression%20Example&var=saxonunsafeplaceholder">Unsafe when Using String Placeholders on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Using%20Bind%20Variables%20on%20XQExpression%20Example&var=saxonsafebind">Safe when Using Bind Variables on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Escaping%20Quotation%20Marks%20and%20Semicolons%20on%20XQExpression%20Example&var=saxonsafeescape">Safe when Escaping Quotation Marks and Semicolons on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XQPreparedExpression%20Example&var=saxonunsafeconcatprep">Unsafe when Using String Concatenation on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XQPreparedExpression%20Example&var=saxonunsafeplaceholderprep">Unsafe when Using String Placeholders on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Using%20Bind%20Variables%20on%20XQPreparedExpression%20Example&var=saxonsafebindprep">Safe when Using Bind Variables on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Escaping%20Quotation%20Marks%20and%20Semicolons%20on%20XQPreparedExpression%20Example&var=saxonsafeescapeprep">Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example</a></li>
				</ul>
				<br />
			</li>
		</ul>
		<br />
	</li>
</ul>

</body>
</html>