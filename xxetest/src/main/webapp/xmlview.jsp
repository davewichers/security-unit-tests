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

<h2 style="color:red">
<%
    final int javaVersionMajor = Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(2, 3));
    int javaVersionUpdate;
    if (Runtime.class.getPackage().getImplementationVersion().length() > 5) {
        javaVersionUpdate = Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(6));
    }
    else {
        javaVersionUpdate = 0;
    }

    // special messages for certain test cases
    if (request.getParameter("var").equals("documentbuildersafeexpansion")) {
        out.println("NOTE: Although this is supposed to make DocumentBuilder safe according to Java documentation, it doesn't");
    }
    if (request.getParameter("var").contains("xom")) {
        out.println("Make sure you comment out XOM's dependency exclusions in the pom.xml before using this test");
    }
    if (request.getParameter("var").contains("access")) {
        if ((javaVersionMajor == 7 && javaVersionUpdate < 40) || javaVersionMajor <= 6) {
            out.println("NOTE: This fix is not available for your current Java version");
        }
    }
%>
</h2>
<h1><%= request.getParameter("test") %></h1>
<a href='codeview.jsp?type=xml&test=<%= request.getParameter("test") %>'>View code for this test</a>
<br /><br />
<h3>
<%
    if (request.getParameter("var").contains("xomsafe")) {
        out.println("XOM will create an XML object that simulates the following XML file:");
    }
    else {
        out.println("Enter an XML file containing an entity:");
    }
%>
</h3>
<textarea title="Payload" rows="15" cols="150" name="payload" form="theform"
<%
    if (request.getParameter("var").contains("xomsafe")) {
        out.println(" readonly=\"readonly\"");
    }
%>
>
<%
    // print out the xml file in the text area
    StringBuilder contentBuilder = new StringBuilder();
    try {
        BufferedReader in;
        if (request.getParameter("var").contains("jaxb")) {
            in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetestbook1web.xml")));
        }
        else if (request.getParameter("var").contains("xmldecoder")) {
            in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetestbeanweb.xml")));
        }
        else {
            in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetest1web.xml")));
        }
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
<br>
<form id="theform" action="results" method="get" autocomplete="off">
    <input type="hidden" name="var" value="<%= request.getParameter("var") %>" />
    <input type="submit" value="Submit">
</form>

<br /><br /><a href="index.jsp">&lt&lt&lt back to tests</a>

</body>
</html>