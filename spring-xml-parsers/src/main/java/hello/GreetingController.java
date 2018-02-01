package hello;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XomDriver;
import com.thoughtworks.xstream.io.xml.BEAStaxDriver;

import org.springframework.util.xml.StaxUtils;
import hello.MyMarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMarshaller;
@Controller
public class GreetingController {
	static ContextSingleton ctx;
	static {
		ctx = ContextSingleton.getInstance();
	}
	
    @RequestMapping("/springStaxUtilsCreateDefensiveInputFactory")
    // Note:
    // Spring
    public String defensiveInputFactory(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    	
    	XMLInputFactory xif = StaxUtils.createDefensiveInputFactory();
    	String ret = "";
    	try {
			XMLStreamReader xsr = xif.createXMLStreamReader(new ByteArrayInputStream(name.getBytes()));
			while (xsr.hasNext())
			{
				xsr.next();
				if (xsr.hasText())
				{
					ret += xsr.getText();
				}
			}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = "Error parsing XML...";//e.getStackTrace().toString();
		}
    	
    	model.addAttribute("name", ret);
        
        return "greeting";	// view template name
    }
    
    @RequestMapping("/springCastorUnmarshaller")
    // Note:
    // Spring
    public String castor(
    		@RequestParam(value="person", required=true) String person,
    		@RequestParam(value="unsafe", required=false, defaultValue="false") boolean unsafe,
    		Model model
    ) {
    	String ret = "default";
 
    	CastorMarshaller mar = (CastorMarshaller) ctx.getBean("castorMarshaller");
    	if (unsafe)
    		mar.setProcessExternalEntities(true);
    	ByteArrayInputStream bytes = new ByteArrayInputStream(person.getBytes());
 	   
 	   try {
 		   Person p = (Person)mar.unmarshal(new StreamSource(bytes));
 		   ret = p.getName();
	} catch (IOException e) {
		// TODO better error handling
		e.printStackTrace();
	}
    	
    	model.addAttribute("name", ret);
        
        return "greeting";	// view template name
    }
    
    
    @RequestMapping("/springJaxb2Unmarshaller")
    // Note:
    // Spring
    public String jaxb2(
    		@RequestParam(value="person", required=true) String person,
    		@RequestParam(value="unsafe", required=false, defaultValue="false") boolean unsafe,
    		Model model
    ) {
    	String ret = "default";
    
    	Jaxb2Marshaller mar = (Jaxb2Marshaller) ctx.getBean("jaxbMarshallerBean");
    	
    	if (unsafe)
    		mar.setProcessExternalEntities(true);
    	ByteArrayInputStream bytes = new ByteArrayInputStream(person.getBytes());
 	   
 	   Person p = (Person)mar.unmarshal(new StreamSource(bytes));
	   ret = p.getName();
	   
	   model.addAttribute("name", ret);
        
       return "greeting";	// view template name
    }
    
    
    @RequestMapping("/springXstreamUnmarshaller")
    // Note:
    // Spring
    public String xstream(
    		@RequestParam(value="person", required=true) String person,
    		@RequestParam(value="unsafe", required=false, defaultValue="false") boolean unsafe,
    		Model model
    ) {
    	String ret = "default";
    
    	XStreamMarshaller mar = (XStreamMarshaller) ctx.getBean("xstreamMarshaller");
    	ByteArrayInputStream bytes = new ByteArrayInputStream(person.getBytes());
    	
    	// by default, the XML Pull Parser is used for XStream
    	// this parser does not process XML entities at all
    	// reference: http://x-stream.github.io/CVE-2016-3674.html
    	
    	// not sure exactly how to make it vulnerable to XXE
//    	if (unsafe)
//    	{
//    		//XomDriver streamDriver = new XomDriver();
//    		BEAStaxDriver streamDriver = new BEAStaxDriver();
//    		mar.setStreamDriver(streamDriver);
//    		mar.setProcessExternalEntities(true);
//    	}
    	
    	
    	Person p;
		try {
			p = (Person)mar.unmarshal(new StreamSource(bytes));
			ret = p.getName();
		} catch (XmlMappingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    	
    	model.addAttribute("name", ret);
        
        return "greeting";	// view template name
    }

}
