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
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XQExpression%20Example&var=xqjsaxonunsafeconcat">Unsafe when Using String Concatenation on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XQExpression%20Example&var=xqjsaxonunsafeplaceholder">Unsafe when Using String Placeholders on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Using%20Bind%20Variables%20on%20XQExpression%20Example&var=xqjsaxonsafebind">Safe when Using Bind Variables on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Whitelisting%20on%20XQExpression%20Example&var=xqjsaxonsafelist">Safe when Whitelisting on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Escaping%20Quotation%20Marks%20and%20Semicolons%20on%20XQExpression%20Example&var=xqjsaxonsafeescape">Safe when Escaping Quotation Marks and Semicolons on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XQPreparedExpression%20Example&var=xqjsaxonunsafeconcatprep">Unsafe when Using String Concatenation on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20Saxon%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XQPreparedExpression%20Example&var=xqjsaxonunsafeplaceholderprep">Unsafe when Using String Placeholders on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Using%20Bind%20Variables%20on%20XQPreparedExpression%20Example&var=xqjsaxonsafebindprep">Safe when Using Bind Variables on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Whitelisting%20on%20XQPreparedExpression%20Example&var=xqjsaxonsafelistprep">Safe when Whitelisting on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20Saxon%3A%20Safe%20when%20Escaping%20Quotation%20Marks%20and%20Semicolons%20on%20XQPreparedExpression%20Example&var=xqjsaxonsafeescapeprep">Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example</a></li>
				</ul>
				<br />
			</li>

			<!-- Not working
			<li>Using <a href="http://xqj.net/basex/">net.xqj.basex.BaseXXQDataSource (link to <b>BaseX</b> page)</a>
				<ul>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20BaseX%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XQExpression%20Example&var=xqjbasexunsafeconcat">Unsafe when Using String Concatenation on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20BaseX%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XQExpression%20Example&var=xqjbasexunsafeplaceholder">Unsafe when Using String Placeholders on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20BaseX%3A%20Safe%20when%20Using%20Bind%20Variables%20on%20XQExpression%20Example&var=xqjbasexsafebind">Safe when Using Bind Variables on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20BaseX%3A%20Safe%20when%20Whitelisting%20on%20XQExpression%20Example&var=xqjbasexsafelist">Safe when Whitelisting on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20BaseX%3A%20Safe%20when%20Escaping%20Quotation%20Marks%20and%20Semicolons%20on%20XQExpression%20Example&var=xqjbasexsafeescape">Safe when Escaping Quotation Marks and Semicolons on XQExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20BaseX%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XQPreparedExpression%20Example&var=xqjbasexunsafeconcatprep">Unsafe when Using String Concatenation on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Unsafe%20XQuery&test=XQJ%20with%20BaseX%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XQPreparedExpression%20Example&var=xqjbasexunsafeplaceholderprep">Unsafe when Using String Placeholders on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20BaseX%3A%20Safe%20when%20Using%20Bind%20Variables%20on%20XQPreparedExpression%20Example&var=xqjbasexsafebindprep">Safe when Using Bind Variables on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20BaseX%3A%20Safe%20when%20Whitelisting%20on%20XQPreparedExpression%20Example&var=xqjbasexsafelistprep">Safe when Whitelisting on XQPreparedExpression Example</a></li>
					<li><a href="xqueryview.jsp?title=Safe%20XQuery&test=XQJ%20with%20BaseX%3A%20Safe%20when%20Escaping%20Quotation%20Marks%20and%20Semicolons%20on%20XQPreparedExpression%20Example&var=xqjbasexsafeescapeprep">Safe when Escaping Quotation Marks and Semicolons on XQPreparedExpression Example</a></li>
				</ul>
				<br />
			</li>
			-->
		</ul>
		<br />
	</li>

	<li><a href="http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/package-summary.html">net.sf.saxon.s9api (link to <b>Saxonica Saxon9</b> Javadoc)</a>
		<ul>
			<li><a href="xqueryview.jsp?title=Unsafe%20Saxon&test=Saxon%3A%20Unsafe%20when%20Using%20String%20Concatenation%20on%20XQuery%20Expression%20Example&var=saxonunsafeconcat">Unsafe when Using String Concatenation on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=Unsafe%20Saxon&test=Saxon%3A%20Unsafe%20when%20Using%20String%20Placeholders%20on%20XQuery%20Expression%20Example&var=saxonunsafeplaceholder">Unsafe when Using String Placeholders on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=Safe%20Saxon&test=Saxon%3A%20Safe%20when%20Using%20Bind%20Variables%20on%20XQuery%20Expression%20Example&var=saxonsafebind">Safe when Using Bind Variables on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=Safe%20Saxon&test=Saxon%3A%20Safe%20when%20Whitelisting%20on%20XQuery%20Expression%20Example&var=saxonsafelist">Safe when Whitelisting on XQuery Expression Example</a></li>
			<li><a href="xqueryview.jsp?title=Safe%20Saxon&test=Saxon%3A%20Safe%20when%20Escaping%20Quotation%20Marks%20and%20Semicolons%20on%20XQuery%20Expression%20Example&var=saxonsafeescape">Safe when Escaping Quotation Marks and Semicolons on XQuery Expression Example</a></li>
		</ul>
	</li>
</ul>

</body>
</html>