<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="org.owasp.encoder.Encode" %>
<html>
<head>
    <title>Code View</title>
</head>
<body>

<%
    //Anti-Caching Controls
    response.setHeader("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1 controls
    response.setHeader("Pragma","no-cache"); //HTTP 1.0 controls
    response.setDateHeader ("Expires", 0); //Prevents caching on proxy servers

    //Anti-Clickjacking Controls
    response.setHeader("X-Frame-Options", "DENY");
%>

<%
    // get to the correct directory for the .java files
    File temp = new File(System.getProperty("user.dir") + "/.cargo");
    StringBuilder path = new StringBuilder(new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent());
    path.append(File.separator).append("src").append(File.separator).append("main").append(File.separator).append("java").append(File.separator).append("com").append(File.separator).append("aspectsecurity").append(File.separator).append("xxetestweb");

    // get to the correct subdirectory for the type of test
    if (request.getParameter("type").equals("xml")) {
        path.append(File.separator).append("xxetestcases");
    }
    else if (request.getParameter("type").equals("xpath")) {
        path.append(File.separator).append("xpathtestcases");
    }
    else if (request.getParameter("type").equals("xquery")) {
        path.append(File.separator).append("xquerytestcases");
    }

    // find the file name of the needed file
    File parentFolder = new File(path.toString());
    String[] fileList = parentFolder.list();
    if (fileList != null) {
        for (String fileName : fileList) {
            if ((fileName.toLowerCase()).equals(request.getParameter("var") + "testcase.java")) {
                path.append(File.separator).append(fileName);
            }
        }
    }

    // read and print the file
    response.getWriter().write("<pre>");
    BufferedReader br = new BufferedReader(new FileReader(path.toString()));
    String line;
    while ((line = br.readLine()) != null) {
        response.getWriter().write(Encode.forHtmlContent(line) + "<br />");
    }
    response.getWriter().write("</pre>");
%>

<br /><br />
<button type="button" name="back" onclick="history.back()">&lt&lt&lt back to test</button>

</body>
</html>
