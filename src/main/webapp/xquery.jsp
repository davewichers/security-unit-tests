<%@ page import="org.owasp.encoder.Encode" %>
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
		out.println("Unsafe Tests: 10 <br> Safe Tests: 15");
	%>
</h3>

<% int testCount = 69;	// for numbering all tests %>

<p>XQuery Implementations:</p>
<ul>
	<li><a href="https://docs.oracle.com/database/121/JAXML/javax/xml/xquery/package-summary.html">javax.xml.xquery (link to <b>XQuery API for Java (XQJ)</b> Javadoc)</a>
		<ul>
			<li>Using <a href="http://www.saxonica.com/html/documentation9.6/javadoc/com/saxonica/xqj/SaxonXQDataSource.html">com.saxonica.xqj.SaxonXQDataSource (link to <b>Saxonica Saxon9</b> Javadoc)</a>
				<ol start="<%= (testCount) %>">
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Unsafe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Unsafe when Using String Concatenation on XQExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonunsafeconcat") %>">Unsafe when Using String Concatenation on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Unsafe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Unsafe when Using String Placeholders on XQExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonunsafeplaceholder") %>">Unsafe when Using String Placeholders on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Safe when Using Bind Variables on XQExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonsafebind") %>">Safe when Using Bind Variables on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Safe when Whitelisting on XQExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonsafelist") %>">Safe when Whitelisting on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Safe when Escaping Quotation Marks and Semicolons on XQExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonsafeescape") %>">Safe when Escaping Quotation Marks and Semicolons on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Unsafe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Unsafe when Using String Concatenation on XQPreparedExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonunsafeconcatprep") %>">Unsafe when Using String Concatenation on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Unsafe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Unsafe when Using String Placeholders on XQPreparedExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonunsafeplaceholderprep") %>">Unsafe when Using String Placeholders on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Safe when Using Bind Variables on XQPreparedExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonsafebindprep") %>">Safe when Using Bind Variables on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Safe when Whitelisting on XQPreparedExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonsafelistprep") %>">Safe when Whitelisting on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe XQuery") %>&test=<%= Encode.forUriComponent("XQJ with Saxon: Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example") %>&var=<%= Encode.forUriComponent("xqjsaxonsafeescapeprep") %>">Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example</a></li>
				</ol>
				<br />
			</li>
		</ul>
		<br />
	</li>

	<li><a href="http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/package-summary.html">net.sf.saxon.s9api (link to <b>Saxonica Saxon9</b> Javadoc)</a>
		<ol start="<%= (testCount + 10) %>">
			<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Unsafe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Unsafe when Using String Concatenation on XQuery Expression Example") %>&var=<%= Encode.forUriComponent("saxonunsafeconcatxquery") %>">Unsafe when Using String Concatenation on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Unsafe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Unsafe when Using String Placeholders on XQuery Expression Example") %>&var=<%= Encode.forUriComponent("saxonunsafeplaceholderxquery") %>">Unsafe when Using String Placeholders on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Safe when Using Bind Variables on XQuery Expression Example") %>&var=<%= Encode.forUriComponent("saxonsafebindxquery") %>">Safe when Using Bind Variables on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Safe when Whitelisting on XQuery Expression Example") %>&var=<%= Encode.forUriComponent("saxonsafelistxquery") %>">Safe when Whitelisting on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=<%= Encode.forUriComponent("Safe Saxon") %>&test=<%= Encode.forUriComponent("Saxon: Safe when Escaping Quotation Marks and Semicolons on XQuery Expression Example") %>&var=<%= Encode.forUriComponent("saxonsafeescapexquery") %>">Safe when Escaping Quotation Marks and Semicolons on XQuery Expression Example</a></li>
		</ol>
	</li>
</ul>

</body>
</html>