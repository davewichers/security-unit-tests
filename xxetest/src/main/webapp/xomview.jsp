<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStreamReader" %>
<html>
<head>
    <title><%= request.getParameter("title") %></title>
</head>
<body>

<%
    // Anti-Caching Controls
    response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");  // HTTP 1.1 controls
    response.setHeader("Pragma","no-cache");    // HTTP 1.0 controls
    response.setDateHeader ("Expires", 0);  // Prevents caching on proxy servers

    // Anti-Clickjacking Controls
    response.setHeader("X-Frame-Options", "DENY");
%>

<h2 style="color:red"></h2>
<h1><%= request.getParameter("test") %></h1>
<a href='codeview.jsp?type=xml&test=<%= request.getParameter("test") %>'>View code for this test</a>
<br /><br />

<form id="theform" action="results" method="get" autocomplete="off">
    <h3>Enter a DOCTYPE declaration containing an external entity:</h3>
    <input type="text" size="100" name="doc" value='<!DOCTYPE test [<!ENTITY xxetest1 SYSTEM "../../../../src/main/resources/xxe.txt"> ]> <filler />' title="DOCTYPE">
    <h3>Enter a name for a generic XML tag:</h3>
    <input type="text" name="tag" value="test" title="Tag">
    <h3>Enter the entity reference for the above tag's content:</h3>
    <input type="text" name="ref" value="&amp;xxetest1;" title="Entity Reference">
    <input type="hidden" name="var" value="<%= request.getParameter("var") %>" />
    <br /><br /><br />
    <input type="submit" value="Submit">
</form>

<br /><br /><a href="index.jsp">&lt&lt&lt back to tests</a>

</body>
</html>