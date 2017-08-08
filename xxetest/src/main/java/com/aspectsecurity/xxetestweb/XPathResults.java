package com.aspectsecurity.xxetestweb;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.xpath.XPathAPI;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.owasp.encoder.Encode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet implementation class XPathResults
 */
@WebServlet("/xpathresults")
public class XPathResults extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public XPathResults() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader("X-Frame-Options", "DENY");    // Anti-Clickjacking Controls

        response.getWriter().write("<html><head><title>Results</title></head><body>");

        /*
         * Detects which test case we're running, runs it, and prints the results
         */
        switch (request.getParameter("var")) {

            //region Stub: Test Case Stub
            /*
             * This is a template for adding test cases
             */
            case "stub": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    // querying the XML

                    // testing the result
                    printResults(expectedSafe, (NodeList) new Object(), response);
                    //printResults(expectedSafe, new ArrayList<>(), response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Xalan: Unsafe when Using String Concatenation on XPath Expression Example
            /*
             * Proves that Xalan is vulnerable to injection when using string concatenation on the XPath expression
             */
            case "xalanunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    //parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
                    NodeList nodeList = XPathAPI.eval(document, query).nodelist();

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Xalan: Unsafe when Using String Placeholders on XPath Expression Example
            /*
             * Proves that Xalan is vulnerable to injection when using string placeholders on the XPath expression
             */
            case "xalanunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    //parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
                    NodeList nodeList = XPathAPI.eval(document, query).nodelist();

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Xalan: Safe when Whitelisting on XPath Expression Example
            /*
             * Proves that XPath is safe from injection when whitelisting on the XPath expression
             */
            case "xalansafelist": {
                final boolean expectedSafe = true;

                try {
                    //parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query;
                    if (request.getParameter("payload").contains("'")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain apostrophes");
                    }
                    else {
                        query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
                    }
                    NodeList nodeList = XPathAPI.eval(document, query).nodelist();

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            // region Xalan: Safe when Escaping Apostrophes on XPath Expression Example
            /*
             * Proves that XPath is safe from injection when using string concatenation while escaping apostrophes on
             * the XPath expression
             */
            case "xalansafeescape": {
                final boolean expectedSafe = true;

                try {
                    //parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
                    NodeList nodeList = XPathAPI.eval(document, query).nodelist();

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region DOM4J: Unsafe when Using String Concatenation on XPath Expression Example
            /*
             * Proves that DOM4J is vulnerable to injection when using string concatenation on the XPath expression
             */
            case "dom4junsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
                    ArrayList<String> nodeList = new ArrayList<>();
                    for (Node node : document.selectNodes(query)) {
                        nodeList.add(node.toString() + "\n" + node.getStringValue());
                    }

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region DOM4J: Unsafe when Using String Placeholders on XPath Expression Example
            /*
             * Proves that DOM4J is vulnerable to injection when using string placeholders on the XPath expression
             */
            case "dom4junsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
                    ArrayList<String> nodeList = new ArrayList<>();
                    for (Node node : document.selectNodes(query)) {
                        nodeList.add(node.toString() + "\n" + node.getStringValue());
                    }

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region DOM4J: Safe when Whitelisting on XPath Expression Example
            /*
             * Proves that DOM4J is safe from injection when whitelisting on the XPath expression
             */
            case "dom4jsafelist": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query;
                    if (request.getParameter("payload").contains("'")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain apostrophes");
                    }
                    else {
                        query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
                    }
                    ArrayList<String> nodeList = new ArrayList<>();
                    for (Node node : document.selectNodes(query)) {
                        nodeList.add(node.toString() + "\n" + node.getStringValue());
                    }

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region DOM4J: Safe when Escaping Apostrophes on XPath Expression Example
            /*
             * Proves that DOM4J is safe from injection when using string concatenation while escaping apostrophes on
             * the XPathExpression
             */
            case "dom4jsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    org.dom4j.Document document = new SAXReader().read(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
                    ArrayList<String> nodeList = new ArrayList<>();
                    for (Node node : document.selectNodes(query)) {
                        nodeList.add(node.toString() + "\n" + node.getStringValue());
                    }

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Java XPath: Unsafe when Using String Concatenation on XPathExpression Example
            /*
             * Proves that XPath is vulnerable to injection when using string concatenation on the XPathExpression
             */
            case "xpathunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
                    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Java XPath: Unsafe when Using String Placeholders on XPathExpression Example
            /*
             * Proves that XPath is vulnerable to injection when using string placeholders on the XPathExpression
             */
            case "xpathunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
                    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Java XPath: Safe when Parameterizing on XPathExpression Example
            /*
             * Proves that XPath is safe from injection when using an XPathVariableResolver to parameterize the XPathExpression
             */
            case "xpathsafeparam": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    final HttpServletRequest finalRequest = request;    // for Java compiler 1.7
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    xPath.setXPathVariableResolver(new XPathVariableResolver() {
                        @Override
                        public Object resolveVariable(QName variableName) {
                            if (variableName.getLocalPart().equals("name")) {
                                return finalRequest.getParameter("payload");
                            }
                            else {
                                return null;
                            }
                        }
                    });
                    String query = "/Students/Student[FirstName/text()=$name]"; // safe!
                    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Java XPath: Safe when Whitelisting on XPathExpression Example
            /*
             * Proves that XPath is safe from injection when whitelisting on the XPathExpression
             */
            case "xpathsafelist": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    String query;
                    if (request.getParameter("payload").contains("'")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain apostrophes");
                    }
                    else {
                        query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
                    }
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Java XPath: Safe when Escaping Apostrophes on XPathExpression Example
            /*
             * Proves that XPath is safe from injection when using string concatenation while escaping apostrophes on
             * the XPathExpression
             */
            case "xpathsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                    Document document = builder.parse(getClass().getResourceAsStream("/students.xml"));

                    // querying the XML
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
                    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(document, XPathConstants.NODESET);

                    // testing the result
                    printResults(expectedSafe, nodeList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Unsafe when Using String Concatenation on XPath Expression Example
            /*
             * Proves that Saxon is vulnerable to injection when using string concatenation on the XPath expression
             */
            case "saxonunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    XPathCompiler compiler = processor.newXPathCompiler();
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
                    XPathExecutable executable = compiler.compile(query);
                    XPathSelector selector = executable.load();
                    selector.setContextItem(node);
                    selector.evaluate();

                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : selector) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Unsafe when Using String Placeholders on XPath Expression Example
            /*
             * Proves that Saxon is vulnerable to injection when using string placeholders on the XPath expression
             */
            case "saxonunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    XPathCompiler compiler = processor.newXPathCompiler();
                    String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
                    XPathExecutable executable = compiler.compile(query);
                    XPathSelector selector = executable.load();
                    selector.setContextItem(node);
                    selector.evaluate();

                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : selector) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Safe when Parameterizing on XPath Expression Example
            /*
             * Proves that Saxon is safe from injection when using a QName to parameterize the XPath expression
             */
            case "saxonsafeparam": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    XPathCompiler compiler = processor.newXPathCompiler();
                    net.sf.saxon.s9api.QName qname = new net.sf.saxon.s9api.QName("name");
                    compiler.declareVariable(qname);
                    String query = "/Students/Student[FirstName/text()=$name]"; // safe!
                    XPathExecutable executable = compiler.compile(query);
                    XPathSelector selector = executable.load();
                    selector.setContextItem(node);
                    selector.setVariable(qname, XdmValue.makeValue(request.getParameter("payload")));
                    selector.evaluate();

                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : selector) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Safe when Whitelisting on XPath Expression Example
            /*
             * Proves that Saxon is safe from injection when whitelisting on the XPath expression
             */
            case "saxonsafelist": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    String query;
                    if (request.getParameter("payload").contains("'")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain apostrophes");
                    }
                    else {
                        query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
                    }
                    XPathCompiler compiler = processor.newXPathCompiler();
                    XPathExecutable executable = compiler.compile(query);
                    XPathSelector selector = executable.load();
                    selector.setContextItem(node);
                    selector.evaluate();

                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : selector) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region Saxon: Safe when Escaping Apostrophes on XPath Expression Example
            /*
             * Proves that Saxon is safe from injection when using string concatenation while escaping apostrophes on
             * the XPath expression
             */
            case "saxonsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // parsing the XML
                    Processor processor = new Processor(false);
                    net.sf.saxon.s9api.DocumentBuilder documentBuilder = processor.newDocumentBuilder();
                    XdmNode node = documentBuilder.build(new StreamSource(getClass().getResourceAsStream("/students.xml")));

                    // querying the XML
                    XPathCompiler compiler = processor.newXPathCompiler();
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
                    XPathExecutable executable = compiler.compile(query);
                    XPathSelector selector = executable.load();
                    selector.setContextItem(node);
                    selector.evaluate();

                    ArrayList<String> resultList = new ArrayList<>();
                    for (XdmValue value : selector) {
                        resultList.add(value.toString());
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region VTD-XML: Unsafe when Using String Concatenation on XPath Expression Example
            /*
             * Proves that VTD-XML is vulnerable to injection when using string concatenation on the XPath expression
             */
            case "vtdunsafeconcat": {
                final boolean expectedSafe = false;

                try {
                    // getting the file
                    File temp = new File(System.getProperty("user.dir") + "/.cargo");
                    String path = new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent();
                    path += File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
                    File file = new File(path + "students.xml");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    fileInputStream.read(bytes);

                    // parsing the XML
                    VTDGen vtdGen = new VTDGen();
                    vtdGen.setDoc(bytes);
                    vtdGen.parse(true);
                    VTDNav vtdNav = vtdGen.getNav();
                    AutoPilot autoPilot = new AutoPilot(vtdNav);

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']"; // unsafe!
                    autoPilot.selectXPath(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    int result;
                    while ((result = autoPilot.evalXPath()) != -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vtdNav.toString(result) + " " + vtdNav.toString(result + 1));
                        vtdNav.toElement(VTDNav.FIRST_CHILD);
                        do {
                            int t = vtdNav.getText(); // get the index of the text (char data or CDATA)
                            if (t != -1)
                                stringBuilder.append("\n\t" + vtdNav.toNormalizedString(t));
                        } while (vtdNav.toElement(VTDNav.NEXT_SIBLING));
                        resultList.add(stringBuilder.toString());
                        vtdNav.toElement(VTDNav.PARENT);
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region VTD-XML: Unsafe when Using String Placeholders on XPath Expression Example
            /*
             * Proves that VTD-XML is vulnerable to injection when using string placeholders on the XPath expression
             */
            case "vtdunsafeplaceholder": {
                final boolean expectedSafe = false;

                try {
                    // getting the file
                    File temp = new File(System.getProperty("user.dir") + "/.cargo");
                    String path = new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent();
                    path += File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
                    File file = new File(path + "students.xml");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    fileInputStream.read(bytes);

                    // parsing the XML
                    VTDGen vtdGen = new VTDGen();
                    vtdGen.setDoc(bytes);
                    vtdGen.parse(true);
                    VTDNav vtdNav = vtdGen.getNav();
                    AutoPilot autoPilot = new AutoPilot(vtdNav);

                    // querying the XML
                    String query = String.format("/Students/Student[FirstName/text()='%s']", request.getParameter("payload")); // unsafe!
                    autoPilot.selectXPath(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    int result;
                    while ((result = autoPilot.evalXPath()) != -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vtdNav.toString(result) + " " + vtdNav.toString(result + 1));
                        vtdNav.toElement(VTDNav.FIRST_CHILD);
                        do {
                            int t = vtdNav.getText(); // get the index of the text (char data or CDATA)
                            if (t != -1)
                                stringBuilder.append("\n\t" + vtdNav.toNormalizedString(t));
                        } while (vtdNav.toElement(VTDNav.NEXT_SIBLING));
                        resultList.add(stringBuilder.toString());
                        vtdNav.toElement(VTDNav.PARENT);
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region VTD-XML: Unsafe when Parameterizing on XPath Expression Example
            /*
             * Proves that VTD-XML can still be injected upon even when parameterizing the XPath expression
             */
            case "vtdunsafeparam": {
                final boolean expectedSafe = false;

                try {
                    // getting the file
                    File temp = new File(System.getProperty("user.dir") + "/.cargo");
                    String path = new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent();
                    path += File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
                    File file = new File(path + "students.xml");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    fileInputStream.read(bytes);

                    // parsing the XML
                    VTDGen vtdGen = new VTDGen();
                    vtdGen.setDoc(bytes);
                    vtdGen.parse(true);
                    VTDNav vtdNav = vtdGen.getNav();
                    AutoPilot autoPilot = new AutoPilot(vtdNav);

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()=$name]"; // safe!
                    autoPilot.declareVariableExpr("name", "'" + request.getParameter("payload") + "'");
                    autoPilot.selectXPath(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    int result;
                    while ((result = autoPilot.evalXPath()) != -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vtdNav.toString(result) + " " + vtdNav.toString(result + 1));
                        vtdNav.toElement(VTDNav.FIRST_CHILD);
                        do {
                            int t = vtdNav.getText(); // get the index of the text (char data or CDATA)
                            if (t != -1)
                                stringBuilder.append("\n\t" + vtdNav.toNormalizedString(t));
                        } while (vtdNav.toElement(VTDNav.NEXT_SIBLING));
                        resultList.add(stringBuilder.toString());
                        vtdNav.toElement(VTDNav.PARENT);
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region VTD-XML: Safe when Whitelisting on XPath Expression Example
            /*
             * Proves that VTD-XML is safe from injection when whitelisting on the XPath expression
             */
            case "vtdsafelist": {
                final boolean expectedSafe = true;

                try {
                    // getting the file
                    File temp = new File(System.getProperty("user.dir") + "/.cargo");
                    String path = new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent();
                    path += File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
                    File file = new File(path + "students.xml");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    fileInputStream.read(bytes);

                    // parsing the XML
                    VTDGen vtdGen = new VTDGen();
                    vtdGen.setDoc(bytes);
                    vtdGen.parse(true);
                    VTDNav vtdNav = vtdGen.getNav();
                    AutoPilot autoPilot = new AutoPilot(vtdNav);

                    // querying the XML
                    String query;
                    if (request.getParameter("payload").contains("'")) {
                        printResults(expectedSafe, new ArrayList<>(), response);
                        throw new InvalidParameterException("First Name parameter must not contain apostrophes");
                    }
                    else {
                        query = "/Students/Student[FirstName/text()='" + request.getParameter("payload") + "']";    // safe in here!
                    }
                    autoPilot.selectXPath(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    int result;
                    while ((result = autoPilot.evalXPath()) != -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vtdNav.toString(result) + " " + vtdNav.toString(result + 1));
                        vtdNav.toElement(VTDNav.FIRST_CHILD);
                        do {
                            int t = vtdNav.getText(); // get the index of the text (char data or CDATA)
                            if (t != -1)
                                stringBuilder.append("\n\t" + vtdNav.toNormalizedString(t));
                        } while (vtdNav.toElement(VTDNav.NEXT_SIBLING));
                        resultList.add(stringBuilder.toString());
                        vtdNav.toElement(VTDNav.PARENT);
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            //region VTD-XML: Safe when Escaping Apostrophes on XPath Expression Example
            /*
             * Proves that VTD-XML is safe from injection when using string concatenation while escaping apostrophes on
             * the XPath expression
             */
            case "vtdsafeescape": {
                final boolean expectedSafe = true;

                try {
                    // getting the file
                    File temp = new File(System.getProperty("user.dir") + "/.cargo");
                    String path = new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent();
                    path += File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
                    File file = new File(path + "students.xml");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    fileInputStream.read(bytes);

                    // parsing the XML
                    VTDGen vtdGen = new VTDGen();
                    vtdGen.setDoc(bytes);
                    vtdGen.parse(true);
                    VTDNav vtdNav = vtdGen.getNav();
                    AutoPilot autoPilot = new AutoPilot(vtdNav);

                    // querying the XML
                    String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
                    autoPilot.selectXPath(query);
                    ArrayList<String> resultList = new ArrayList<>();
                    int result;
                    while ((result = autoPilot.evalXPath()) != -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vtdNav.toString(result) + " " + vtdNav.toString(result + 1));
                        vtdNav.toElement(VTDNav.FIRST_CHILD);
                        do {
                            int t = vtdNav.getText(); // get the index of the text (char data or CDATA)
                            if (t != -1)
                                stringBuilder.append("\n\t" + vtdNav.toNormalizedString(t));
                        } while (vtdNav.toElement(VTDNav.NEXT_SIBLING));
                        resultList.add(stringBuilder.toString());
                        vtdNav.toElement(VTDNav.PARENT);
                    }

                    // testing the result
                    printResults(expectedSafe, resultList, response);

                } catch (Exception ex) {
                    response.getWriter().write(ex.toString());
                }

                break;
            }
            //endregion

            default:
                response.getWriter().write("Error: Test case not found for \"" + request.getParameter("var") + "\"");
                break;
        }

        response.getWriter().write("<br /><br /><br /><a href=\"xpath.jsp\">&lt&lt&lt back to tests</a>");
        response.getWriter().write("</body></html>");
    }

    /**
     * Prints the results of the XPath test case that uses NodeList
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param nodeList	    the result of the XPath query
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    private void printResults(boolean expectedSafe, NodeList nodeList, HttpServletResponse response) throws IOException {

        boolean actuallySafe = true;
        if (nodeList.getLength() > 1) {
            actuallySafe = false;
        }

        response.getWriter().write("<h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XPath query is safe! :)" : "Unsafe query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student or empty result" : "Should be all Students") + "):</b><br /><pre>");

        // print all Node information
        for (int i = 0; i < nodeList.getLength(); i++) {
            response.getWriter().write("Student " + nodeList.item(i).getAttributes().getNamedItem("studentId").getTextContent() + ":<br />");
            for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                switch (j) {
                    case 1: response.getWriter().write("\tLast Name:\t");   break;
                    case 3: response.getWriter().write("\tFirst Name:\t");  break;
                    case 5: response.getWriter().write("\tUsername:\t");    break;
                    case 7: response.getWriter().write("\tPassword:\t");    break;
                }
                if (!((j % 2) == 0)) {
                    response.getWriter().write(nodeList.item(i).getChildNodes().item(j).getTextContent() + "<br />");
                }
            }
        }

        response.getWriter().write("</pre>");
    }

    /**
     * Prints the results of the Saxon XPath test case
     * @param expectedSafe	whether the test is supposed to be safe or not
     * @param resultList	the result of the Saxon XPath query
     * @param response		the servlet response
     * @throws IOException	in case the response has an error
     */
    private void printResults(boolean expectedSafe, ArrayList<String> resultList, HttpServletResponse response) throws IOException {

        boolean actuallySafe = true;
        if (resultList.size() > 1) {
            actuallySafe = false;
        }

        response.getWriter().write("<h3>");
        response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />");
        response.getWriter().write("Actual result: " + (actuallySafe ? "XPath query is safe! :)" : "Unsafe query was injected! :(") + "</h3><br /><br />");
        response.getWriter().write("<b>Results of Query (" + (actuallySafe ? "Should only be the entered Student or empty result" : "Should be all Students") + "):</b><br /><pre>");

        for (String student : resultList) {
            response.getWriter().write(Encode.forHtmlContent(student) + "<br />");
        }

        response.getWriter().write("</pre>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
