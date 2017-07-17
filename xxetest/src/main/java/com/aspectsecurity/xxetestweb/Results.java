package com.aspectsecurity.xxetestweb;

import com.aspectsecurity.xxetest.SAXHandler;
import com.aspectsecurity.xxetest.jaxb.BookType;
import com.aspectsecurity.xxetest.jaxb.Collection;
import nu.xom.Builder;
import nu.xom.DocType;
import nu.xom.Element;
import org.owasp.encoder.Encode;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class Results
 */
@WebServlet("/results")
public class Results extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int javaVersionMajor = Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(2, 3));
	private static final int javaVersionUpdate = getJavaUpdateVersion();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Results() {
        super();
    }

    /*
     * Gets the Java update version for current runtime
     */
    private static int getJavaUpdateVersion() {
		if (Runtime.class.getPackage().getImplementationVersion().length() > 5) {
			return Integer.parseInt(Runtime.class.getPackage().getImplementationVersion().substring(6));
		}
		else {
			return 0;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setHeader("X-Frame-Options", "DENY");	// Anti-Clickjacking Controls

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

				// parsing the XML
				try {
					// testing the result
					printResults(expectedSafe, "", response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region DocumentBuilder: Unsafe by Default Example
			/*
			 * Proves that DocumentBuilderFactory parses entities by default
			 */
			case "documentbuilderunsafedefault": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					// testing the result
					printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region DocumentBuilder: "Safe" when Disabling Entity Expansion Example (FAILURE)
			/*
			 * By setting the DocumentBuilderFactory's setExpandEntityReferences to false, it is supposed to ignore DTDs,
			 * however, it does not
			 */
			case "documentbuildersafeexpansion": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					docBuilderFactory.setExpandEntityReferences(false);	// supposed to be safe but isn't!
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					// testing the result
					printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region DocumentBuilder: Safe when Disallowing DOCTYPE Declarations Example
			/*
			 * Proves that disallowing DOCTYPE declarations for the DocumentBuilderFactory makes the DocumentBuilder
			 * throw an exception when it sees a DTD
			 */
			case "documentbuildersafedoctype": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);		// safe!
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					// testing the result
					printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region DocumentBuilder: Safe when Disabling External General Entities Example
			/*
			 * Proves that disabling external general entities for the DocumentBuilderFactory makes the DocumentBuilder
			 * ignore DTDs
			 */
			case "documentbuildersafeexternal": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);	// safe!
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					// testing the result
					printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region DocumentBuilder: Unsafe when Enabling External General Entities Example
			/*
			 * Proves that enabling external general entities for the DocumentBuilderFactory leaves the DocumentBuilder
			 * unsafe from malicious entities
			 */
			case "documentbuilderunsafeexternal": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);	// unsafe!
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					// testing the result
					printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region DocumentBuilder: Unsafe when Disabling Validation Example
			/*
			 * Proves that disabling validation for the DocumentBuilderFactory leaves the DocumentBuilder parsing entities
			 */
			case "documentbuilderunsafevalidationoff": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					docBuilderFactory.setValidating(false);	// not safe!
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					// testing the result
					printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region DocumentBuilder: Unsafe when Enabling Validation Example
			/*
			 * Proves that enabling validation for the DocumentBuilderFactory leaves the DocumentBuilder parsing entities
			 */
			case "documentbuilderunsafevalidationon": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					docBuilderFactory.setValidating(true);	// not safe!
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					// testing the result
					printResults(expectedSafe, doc.getDocumentElement().getTextContent(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region JAXBContext: Unsafe (Safe in Java 1.8 and up) JAXBContext Unmarshaller from File Example
			/*
			 * Proves that XML deserialized using a JAXB Unmarshaller is unsafe from malicious entities when
			 * unmarshalled directly from File (Safe in 1.8 and up)
			 */
			case "jaxbunsafefile": {
				boolean expectedSafe = false;
				if (javaVersionMajor >= 8) {
					expectedSafe = true;
				}

				// parsing the XML
				try {

					JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
					Unmarshaller unmarshaller = jc.createUnmarshaller();

					Collection collection= (Collection)
							unmarshaller.unmarshal(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					Collection.Books booksType = collection.getBooks();
					List<BookType> bookList = booksType.getBook();

					String discount = "";
					for (BookType book : bookList) {
						discount = book.getPromotion().getDiscount().trim();
					}

					// testing the result
					printResults(expectedSafe, discount, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region JAXBContext: Unsafe JAXBContext Unmarshaller from Unsafe XMLInputFactory Example
			/*
			 * Proves that XML deserialized using a JAXB Unmarshaller is unsafe from malicious entities when
			 * unmarshalled through an XMLStreamReader from an unsafe XMLInputFactory
			 */
			case "jaxbunsafexmlinputfactory": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {

					JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
					Unmarshaller unmarshaller = jc.createUnmarshaller();
					XMLInputFactory factory = XMLInputFactory.newInstance();
					//factory.setProperty("javax.xml.stream.supportDTD", true);	// this is the default

					Collection collection= (Collection)
							unmarshaller.unmarshal(factory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

					Collection.Books booksType = collection.getBooks();
					List<BookType> bookList = booksType.getBook();

					String discount = "";
					for (BookType book : bookList) {
						discount = book.getPromotion().getDiscount().trim();
					}

					// testing the result
					printResults(expectedSafe, discount, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region JAXBContext: Safe JAXBContext Unmarshaller from Safe XMLInputFactory Example
			/*
			 * Proves that XML deserialized using a JAXB Unmarshaller is safe from malicious entities when
			 * unmarshalled through an XMLStreamReader from a safe XMLInputFactory. It does throw by throwing an
			 * exception when seeing an external entity reference.
			 */
			case "jaxbsafexmlinputfactory": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {

					JAXBContext jc = JAXBContext.newInstance("com.aspectsecurity.xxetest.jaxb");
					Unmarshaller unmarshaller = jc.createUnmarshaller();
					XMLInputFactory factory = XMLInputFactory.newInstance();
					factory.setProperty("javax.xml.stream.supportDTD", false);

					Collection collection= (Collection)
							unmarshaller.unmarshal(factory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

					Collection.Books booksType = collection.getBooks();
					List<BookType> bookList = booksType.getBook();

					String discount = "";
					for (BookType book : bookList) {
						discount = book.getPromotion().getDiscount().trim();
					}

					// testing the result
					printResults(expectedSafe, discount, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region SAXParser: Unsafe by Default Example
			/*
			 * Proves that SAXParserFactory parses entities by default
			 */
			case "saxunsafedefault": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();

					saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);	// unsafe!

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region SAXParser: Safe when Disallowing DOCTYPE Declarations Example
			/*
			 * Proves that disallowing DOCTYPE declarations for the SAXParserFactory makes the SAXParser throw an
			 * exception when it sees a DTD
			 */
			case "saxsafedoctype": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);	// safe!
					SAXParser saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();

					saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region SAXParser: Unsafe when Allowing DOCTYPE Declarations Example
			/*
			 * Proves that allowing DOCTYPE declarations for the SAXParserFactory allows the SAXParser to parse entities
			 */
			case "saxunsafedoctype": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);	// unsafe!
					SAXParser saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();

					saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region SAXParser: Safe when Disabling External General and Parameter Entities Example
			/*
			 * Proves that disabling external general and parameter entities for the SAXParserFactory makes the
			 * SAXParser ignore DTDs
			 */
			case "saxsafeexternal": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);	// safe!
					saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);	// safe!
					SAXParser saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();

					saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region SAXParser: Unsafe when Enabling External General and Parameter Entities Example
			/*
			 * Proves that enabling external general and parameter entities for the SAXParserFactory makes the SAXParser parse entities
			 */
			case "saxunsafeexternal": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);		// unsafe!
					saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);	// unsafe!
					SAXParser saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();

					saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region SAXParser: Unsafe when Disabling Validation Example
			/*
			 * Proves that disabling validation for the SAXParserFactory leaves the SAXParser parsing entities
			 */
			case "saxunsafevalidationoff": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					saxParserFactory.setValidating(false);	// not safe!
					SAXParser saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();

					saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region SAXParser: Unsafe when Enabling Validation Example
			/*
			 * Proves that enabling validation for the SAXParserFactory leaves the SAXParser parsing entities
			 */
			case "saxunsafevalidationon": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					saxParserFactory.setValidating(true);	// not safe!
					SAXParser saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();

					saxParser.parse (new ByteArrayInputStream(request.getParameter("payload").getBytes()), handler);

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region Schema: Unsafe by Default Example
			/*
			 * Proves that SchemaFactory parses entities by default
			 */
			case "schemaunsafe": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {

					SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					Schema schema = factory.newSchema(getClass().getResource("/test.xsd"));
					Validator validator = schema.newValidator();

					StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					StreamResult output = new StreamResult(baos);

					validator.validate(input, output);	// unsafe!
					String result = new String(baos.toByteArray());

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region Schema: Safe when Disallowing External DTDs and Schemas in Java 7u40 and up Example
			/*
			 * Proves that setting SchemaFactory's ACCESS_EXTERNAL_DTD and ACCESS_EXTERNAL_SCHEMA properties makes the
			 * Validator throw an exception when encountering a DTD (Java 7u40 and up only feature)
			 */
			case "schemasafeaccess": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {

					SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");		// safe!
					factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");	// safe!
					Schema schema = factory.newSchema(getClass().getResource("/test.xsd"));
					Validator validator = schema.newValidator();

					StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					StreamResult output = new StreamResult(baos);

					validator.validate(input, output);
					String result = new String(baos.toByteArray());

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region Schema: Safe when Disallowing External DTDs and Schemas on the Validator in Java 7u40 and up Example
			/*
			 * Proves that setting Validator's ACCESS_EXTERNAL_DTD and ACCESS_EXTERNAL_SCHEMA properties makes the
			 * Validator throw an exception when encountering a DTD (Java 7u40 and up only feature)
			 */
			case "schemasafeaccessvalidator": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {

					SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					Schema schema = factory.newSchema(getClass().getResource("/test.xsd"));
					Validator validator = schema.newValidator();
					validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");	// safe!
					validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");	// safe!

					StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					StreamResult output = new StreamResult(baos);

					validator.validate(input, output);
					String result = new String(baos.toByteArray());

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region Transformer: Unsafe by Default Example
			/*
			 * Proves that TransformerFactory parses entities by default
			 */
			case "transformerunsafedefault": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					TransformerFactory factory = TransformerFactory.newInstance();
					Transformer transformer = factory.newTransformer();

					StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					StreamResult output = new StreamResult(baos);

					transformer.transform(input, output);	// unsafe!
					String result = new String(baos.toByteArray());

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region Transformer: Safe when Disallowing DTDs though a Safe XMLInputFactory Reader Example
			/*
			 * Proves that disabling support for DTD in an XMLInputFactory and then transforming the XML through a safe
			 * reader from the XMLInputFactory makes it throw an exception when it sees an external entity reference
			 */
			case "transformersafedtd": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					TransformerFactory factory = TransformerFactory.newInstance();
					Transformer transformer = factory.newTransformer();

					XMLInputFactory inputFactory = XMLInputFactory.newFactory();
					inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);  // safe!

					XMLStreamReader streamReader = inputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));
					StAXSource input = new StAXSource(streamReader);

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					StreamResult output = new StreamResult(baos);

					transformer.transform(input, output);
					String result = new String(baos.toByteArray());

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region Transformer: Safe when Disallowing External DTDs in Java 7u40 and up Example
			/*
			 * Proves that setting TransformerFactory's ACCESS_EXTERNAL_DTD attribute to null makes the Transformer
			 * throw an exception when it sees an external entity reference (Java 7u40 and up only feature)
			 */
			case "transformersafeaccess": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					TransformerFactory factory = TransformerFactory.newInstance();
					factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");	// safe!
					Transformer transformer = factory.newTransformer();

					StreamSource input = new StreamSource(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					StreamResult output = new StreamResult(baos);

					transformer.transform(input, output);
					String result = new String(baos.toByteArray());

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLDecoder: Always Safe (Always Unsafe in Java 1.7u45 and earlier) Example
			/*
			 * Proves that XMLDecoder parses entities in Java versions 1.7u45 and earlier and does not in Java versions
			 * 1.7u51 and later.
			 * In Java versions 1.7u45 and earlier, the com.sun.beans.decoder.DocumentHandler that belongs to the
			 * XMLDecoder object overrides the resolveEntity() method from the org.xml.sax.helpers.DefaultHandler class
			 * (which is implemented from the org.xml.sax.EntityResolver interface) in order to get it to actually
			 * resolve entities. In Java versions 1.7u51 and later, this method implementation is removed, causing it to
			 * default to DefaultHandler's implementation which is a no op.
			 * Since this is purely based in the XMLDecoder source code, there is no way to make it safe from malicious
			 * entities in Java 1.7u45 and earlier, and no way to force it to be unsafe in Java 1.7u51 and later.
			 */
			case "xmldecoderunsafe": {
				boolean expectedSafe = true;
				if ((javaVersionMajor == 7 && javaVersionUpdate <= 45) || javaVersionMajor <= 6) {
					expectedSafe = false;
				}

				// parsing the XML
				try {
					XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(request.getParameter("payload").getBytes()));
					XMLTestBean result = (XMLTestBean) decoder.readObject();
					decoder.close();

					// testing the result
					printResults(expectedSafe, result.getElement(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLInputFactory: Unsafe by Default Example
			/*
			  Proves that XMLInputFactory parses entities by default
			 */
			case "xmlinputunsafedefault": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

					XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					String result = "";
					while(xmlStreamReader.hasNext()) {
						xmlStreamReader.next();
						if (xmlStreamReader.isStartElement()) {
							if (xmlStreamReader.getLocalName().equals("test")) {
								result = xmlStreamReader.getElementText();
							}
						}
					}

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLInputFactory: Safe when Disallowing External DTDs in Java 7u40 and up Example
			/*
			 * Proves that setting XMLInputFactory's ACCESS_EXTERNAL_DTD attribute to null makes the XMLStreamReader
			 * throw an exception when it sees an external entity reference (Java 7u40 and up only feature)
			 */
			case "xmlinputsafeaccess": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
					xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");

					XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					String result = "";
					while(xmlStreamReader.hasNext()) {
						xmlStreamReader.next();
						if (xmlStreamReader.isStartElement()) {
							if (xmlStreamReader.getLocalName().equals("test")) {
								result = xmlStreamReader.getElementText();
							}
						}
					}

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLInputFactory: Safe when Disallowing External Entities Example
			/*
			 * Proves that setting XMLInputFactory's IS_SUPPORTING_EXTERNAL_ENTITIES attribute to false makes the
			 * XMLStreamReader ignore external entity references
			 */
			case "xmlinputsafeexternal": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
					xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

					XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					String result = "";
					while(xmlStreamReader.hasNext()) {
						xmlStreamReader.next();
						if (xmlStreamReader.isStartElement()) {
							if (xmlStreamReader.getLocalName().equals("test")) {
								result = xmlStreamReader.getElementText();
							}
						}
					}

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLInputFactory: Unsafe when Disabling Validation Example
			/*
			 * Proves that disabling validation for the XMLInputFactoryFactory leaves the XMLStreamReader parsing entities
			 */
			case "xmlinputunsafevalidationoff": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
					xmlInputFactory.setProperty("javax.xml.stream.isValidating", "false");

					XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					String result = "";
					while(xmlStreamReader.hasNext()) {
						xmlStreamReader.next();
						if (xmlStreamReader.isStartElement()) {
							if (xmlStreamReader.getLocalName().equals("test")) {
								result = xmlStreamReader.getElementText();
							}
						}
					}

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLInputFactory: Unsafe when Enabling Validation Example
			/*
			 * Proves that enabling validation for the XMLInputFactoryFactory leaves the XMLStreamReader parsing entities
			 */
			case "xmlinputunsafevalidationon": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
					xmlInputFactory.setProperty("javax.xml.stream.isValidating", "true");

					XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					String result = "";
					while(xmlStreamReader.hasNext()) {
						xmlStreamReader.next();
						if (xmlStreamReader.isStartElement()) {
							if (xmlStreamReader.getLocalName().equals("test")) {
								result = xmlStreamReader.getElementText();
							}
						}
					}

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLInputFactory: Safe when Disabling DTD Support Example
			/*
			 * Proves that disabling DTD support for the XMLInputFactory makes the XMLStreamReader throw an exception
			 * when it sees an external entity reference
			 */
			case "xmlinputsafedtd": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
					xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

					XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(request.getParameter("payload").getBytes()));

					String result = "";
					while(xmlStreamReader.hasNext()) {
						xmlStreamReader.next();
						if (xmlStreamReader.isStartElement()) {
							if (xmlStreamReader.getLocalName().equals("test")) {
								result = xmlStreamReader.getElementText();
							}
						}
					}

					// testing the result
					printResults(expectedSafe, result, response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLReader: Unsafe by Default Example
			/*
			 * Proves that XMLReader parses entities by default
			 */
			case "xmlreaderunsafedefault": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxParserFactory.newSAXParser();

					SAXHandler handler = new SAXHandler();
					XMLReader reader = saxParser.getXMLReader();
					reader.setContentHandler(handler);

					reader.parse(new InputSource(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLReader: Safe when Disallowing DOCTYPE Declarations Example
			/*
			 * Proves that disallowing DOCTYPE declarations for the XMLReader makes it throw an exception when it
			 * sees a DTD
			 */
			case "xmlreadersafedoctype": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxParserFactory.newSAXParser();

					SAXHandler handler = new SAXHandler();
					XMLReader reader = saxParser.getXMLReader();
					reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
					reader.setContentHandler(handler);

					reader.parse(new InputSource(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLReader: Unsafe when Allowing DOCTYPE Declarations Example
			/*
			 * Proves that allowing DOCTYPE declarations for the XMLReader allows it to parse entities
			 */
			case "xmlreaderunsafedoctype": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxParserFactory.newSAXParser();

					SAXHandler handler = new SAXHandler();
					XMLReader reader = saxParser.getXMLReader();
					reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
					reader.setContentHandler(handler);

					reader.parse(new InputSource(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLReader: Safe when Disabling External General and Parameter Entities Example
			/*
			 * Proves that disabling external general and parameter entities for the XMLReader makes it ignore DTDs
			 */
			case "xmlreadersafeexternal": {
				final boolean expectedSafe = true;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxParserFactory.newSAXParser();

					SAXHandler handler = new SAXHandler();
					XMLReader reader = saxParser.getXMLReader();
					reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
					reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
					reader.setContentHandler(handler);

					reader.parse(new InputSource(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XMLReader: Unsafe when Enabling External General and Parameter Entities Example
			/*
			 * Proves that enabling external general and parameter entities for the XMLReader makes it parse entities
			 */
			case "xmlreaderunsafeexternal": {
				final boolean expectedSafe = false;

				// parsing the XML
				try {
					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxParserFactory.newSAXParser();

					SAXHandler handler = new SAXHandler();
					XMLReader reader = saxParser.getXMLReader();
					reader.setFeature("http://xml.org/sax/features/external-general-entities", true);
					reader.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
					reader.setContentHandler(handler);

					reader.parse(new InputSource(new ByteArrayInputStream(request.getParameter("payload").getBytes())));

					// testing the result
					printResults(expectedSafe, handler.getTestValue(), response);
				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XOM: Safe by Default Example
			/*
			 * I emailed Elliote Rusty Harold, creator of XOM, about this test case and he said, "There's no way to
			 * insert an entity reference directly in XOM." Therefore, XOM is safe from injection if using the methods
			 * found in this test.
			 */
			case "xomsafe": {
				final boolean expectedSafe = true;

				// creating the parser
				Element test = new Element("test");
				test.appendChild("&xxetest1;");	// appends text, not an actual entity reference, making this safe
				nu.xom.Document doc = new nu.xom.Document(test);
				String dtd = "<!DOCTYPE test [<!ENTITY xxetest1 SYSTEM \"../../../../src/main/resources/xxe.txt\"> ]> <filler />";
				Builder builder = new Builder();
				nu.xom.Document newDoc;

				// parsing the XML
				try {
					newDoc = builder.build(dtd, null);
					DocType doctype = newDoc.getDocType();
					doctype.detach();
					doc.setDocType(doctype);

					// testing the result
					printResults(expectedSafe, doc.toXML(), response);

				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XOM: Unsafe when using an InputStream Example
			/*
			 * When building the XOM Document from an unsafe XML InputStream, the Document parses the DTD.
			 */
			case "xomunsafeinputstream": {
				final boolean expectedSafe = false;

				// creating the parser
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser;

				// parsing the XML
				try {
					saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();
					XMLReader xreader = saxParser.getXMLReader();
					xreader.setContentHandler(handler);

					Builder builder = new Builder();
					nu.xom.Document doc = builder.build(new ByteArrayInputStream(request.getParameter("payload").getBytes()));	// unsafe!

					// testing the result
					printResults(expectedSafe, doc.toXML(), response);

				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			//region XOM: Unsafe when Building from an Unsafe XMLReader Example
			/*
			 * When building the XOM Document from an unsafe XML InputStream, the Document parses the DTD.
			 */
			case "xomunsafexmlreader": {
				final boolean expectedSafe = false;

				// creating the parser
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser;

				// parsing the XML
				try {
					saxParser = saxParserFactory.newSAXParser();
					SAXHandler handler = new SAXHandler();
					XMLReader xreader = saxParser.getXMLReader();
					xreader.setContentHandler(handler);

					Builder builder = new Builder(xreader);
					nu.xom.Document doc = builder.build(new ByteArrayInputStream(request.getParameter("payload").getBytes()));	// unsafe!

					// testing the result
					printResults(expectedSafe, doc.toXML(), response);

				}
				catch (Exception ex) {
					printResults(expectedSafe, ex, response);	// safe: exception thrown when parsing XML
				}

				break;
			}
			//endregion

			default:
				response.getWriter().write("<html><head><title>Error</title></head><body>");
				response.getWriter().write("Error: Test case not found for \"" + request.getParameter("var") + "\"");
				response.getWriter().write("</body></html>");
				break;
		}
	}

	/**
	 * Prints the results of the test case
	 * @param expectedSafe	whether the test is supposed to be safe or not
	 * @param xmlContent	the contents of the XML after parsing
	 * @param response		the servlet response
	 * @throws IOException	in case the response has an error
	 */
	private void printResults(boolean expectedSafe, String xmlContent, HttpServletResponse response) throws IOException {

		response.getWriter().write("<html><head><title>Results</title></head><body><h3>");
		response.getWriter().write("Expected result: " + (expectedSafe ? "Safe" : "Unsafe") + "<br />" + "Actual Result: ");

		if (xmlContent.contains("SUCCESSFUL") || xmlContent.startsWith("SUCC")) {
			response.getWriter().write("Unsafe! XXE was injected! :( </h3><br />" +
					"<b>" + " XML Content (Should contain \"SUCCESSFUL\" or your custom XML entity):" +
					"</b><br />" + Encode.forHtmlContent(xmlContent));
		}
		else {
			response.getWriter().write("XML Parser is safe! :) </h3><br />");
			if (xmlContent.equals("") || xmlContent.isEmpty()) {
				response.getWriter().write("<b>" + "XML Content:" + "</b>" + "<br />" + "The XML file is blank" + "<br /><br />");
			}
			else if (xmlContent.startsWith("XML was not parsed")) {
				response.getWriter().write("<b>" + "XML Content:" + "</b>" + "<br />" + xmlContent);
			}
			else {
				response.getWriter().write("<b>" + "XML Content:" + "</b>" + "<br />" + Encode.forHtmlContent(xmlContent));
			}
		}

		response.getWriter().write("<br /><br /><br /><a href=\"index.jsp\">&lt&lt&lt back to tests</a>");
		response.getWriter().write("</body></html>");
	}

	/**
	 * Prints the results of the test case if there is an exception
	 * @param expectedSafe	whether the test is supposed to be safe or not
	 * @param ex			the exception thrown
	 * @param response		the servlet response
	 * @throws IOException	in case the response has an error
	 */
	private void printResults(boolean expectedSafe, Exception ex, HttpServletResponse response) throws IOException {
		printResults(expectedSafe, "XML was not parsed due to a thrown exception" + "<b><br />" +"Stack Trace: " + "</b><br />" + ex.toString(), response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}