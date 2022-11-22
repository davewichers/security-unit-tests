<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStreamReader" %>
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
    final int javaVersionMajor = Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(2, 3));
    int javaVersionUpdate;
    if (Runtime.class.getPackage().getImplementationVersion().length() > 5) {
        javaVersionUpdate = Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(6));
    }
    else {
        javaVersionUpdate = 0;
    }

    // special messages for certain test cases
    if (request.getParameter("var").equals("documentbuildersafeexpand")) {
        out.println("NOTE: Although this is supposed to make DocumentBuilder safe according to Java documentation, it doesn't");
    }
    if (request.getParameter("var").contains("access")) {
        if ((javaVersionMajor == 7 && javaVersionUpdate < 40) || javaVersionMajor <= 6) {
            out.println("NOTE: This fix is not available for your current Java version");
        }
    }
%>
</h2>
<h1><%= Encode.forHtml(request.getParameter("test")) %></h1>
<%
	String external = request.getParameter("external");
	String url = "codeview.jsp?type=xml&var=" + Encode.forHtml(request.getParameter("var"));
	if (external != null)
		url += "&external=" + external;
%>
<a href='<%= url %>'>View code for this test</a>
<br /><br />
<h3>Enter an XML file containing an entity:</h3>
<textarea title="Payload" rows="15" cols="150" name="payload" form="theform">
<%
    // print out the xml file in the text area
    StringBuilder contentBuilder = new StringBuilder();
    try {
        BufferedReader in;
	external = request.getParameter("external");	// requests made to an external app
	if (external != null && external.contains("spring"))
	{
	    // spring endpoints set up to unmarshal a <person><name></name></person>
	    in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetest_person.xml")));
	}
	else { 
	    if (request.getParameter("var").contains("jaxb")) {
		in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetestbook1web.xml")));
	    }
	    else if (request.getParameter("var").contains("xmldecoder")) {
		in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetestbeanweb.xml")));
	    }
        else if (request.getParameter("var").contains("xmlparameter")) {
            in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetest2web.xml")));
        }
	    else {
		in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/xxetest1web.xml")));
	    }
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
<br />
<form id="theform" action="<%= Encode.forHtml(request.getParameter("var")) %>" method="get" autocomplete="off">
<% if (request.getParameter("unsafe") != null && request.getParameter("unsafe").equalsIgnoreCase("true")) { %>
    <input type="hidden" name="unsafe" value="true" />
<% } %>
    <input type="hidden" name="var" value="<%= Encode.forHtml(request.getParameter("var")) %>" />
    <input type="submit" value="Submit">
</form>

<br /><br /><a href="index.jsp">&lt;&lt;&lt; back to tests</a>

</body>
</html>
