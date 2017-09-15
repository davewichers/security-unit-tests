<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="org.owasp.encoder.Encode" %>
<html>
<head>
    <title><%= Encode.forHtml(request.getParameter("title")) %></title>
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

<h2 style="color:red">
<%
    // special messages for certain test cases
%>
</h2>
<h1><%= Encode.forHtml(request.getParameter("test")) %></h1>
<a href='codeview.jsp?type=xpath&var=<%= request.getParameter("var") %>'>View code for this test</a>
<br /><br />
<h3>The following is the XML file the query will be performed on:</h3>
<textarea title="Students" rows="15" cols="150" name="payload" form="theform" disabled>
<%
    // print out the xml file in the text area
    StringBuilder contentBuilder = new StringBuilder();
    try {
        BufferedReader in;
        in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/students.xml")));
        String str;
        while ((str = in.readLine()) != null) {
            contentBuilder.append(str);
            contentBuilder.append("\n");
        }
        in.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    out.println(contentBuilder.toString());
%>
</textarea>
<br />
<form id="theform" action="<%= Encode.forHtml(request.getParameter("var")) %>" autocomplete="off">
    <input type="hidden" name="var" value="<%= Encode.forHtml(request.getParameter("var")) %>" />
    <h3>The injection given below will attempt to fetch all &lt;Student&gt; nodes instead of just the entered one by adding <mark>&apos; or &apos;a&apos;=&apos;a</mark> to the end.</h3>
    Enter first name: <input title="Payload" name="payload" value="Bobby&apos; or &apos;a&apos;=&apos;a" />
    <input type="submit" value="Submit">
</form>

<br /><br /><a href="xpath.jsp">&lt&lt&lt back to tests</a>

</body>
</html>