<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.util.ArrayList" %>
<html>
<head>
    <title>Code View</title>
</head>
<body>

<%
    ArrayList<String> code = new ArrayList<>();
    String line;

    // get to the correct directory for the .java file
    File temp = new File(System.getProperty("user.dir") + "/.cargo");
    String path = new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent();
    path += File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" +
            File.separator + "aspectsecurity" + File.separator + "xxetestweb";

    if (request.getParameter("type").equals("xml")) {
        BufferedReader br = new BufferedReader(new FileReader(path + File.separator + "Results.java"));
        while ((line = br.readLine()) != null) {
            code.add(line);
        }
    }

    response.getWriter().write("<pre>");
    boolean printFlag = false;
    for (String s : code) {
        if (s.contains(request.getParameter("test"))) {
            printFlag = true;
        }

        if (printFlag) {
            response.getWriter().write(s + "<br />");
        }

        if (s.contains("//endregion")) {
            printFlag = false;
        }
    }
    response.getWriter().write("</pre>");
%>

<br /><br />
<button type="button" name="back" onclick="history.back()">&lt&lt&lt back to test</button>

</body>
</html>
