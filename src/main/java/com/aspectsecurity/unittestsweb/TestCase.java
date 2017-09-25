package com.aspectsecurity.unittestsweb;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class TestCase extends HttpServlet {

    /**
     * Gets the major version of the current Java runtime
     * @return the major version of the current Java runtime
     */
    protected static int getJavaVersionMajor() {
        return Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(2, 3));
    }

    /**
     * Gets the update version of the current Java runtime
     * @return the update version of the current Java runtime
     */
    protected static int getJavaVersionUpdate() {
        if (Runtime.class.getPackage().getImplementationVersion().length() > 5) {
            return Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(6));
        }
        else {
            return 0;
        }
    }

    /**
     * The abstract method in which the actual test case will be performed.
     * @param request       the calling HTTP request
     * @param response      the outgoing HTTP response
     * @throws IOException  if there is an I/O issue in test cases that use external files
     */
    protected abstract void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * {@inheritDoc}
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("X-Frame-Options", "DENY");	// Anti-Clickjacking Controls
        doTest(request, response);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
