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
 		   // TODO did it unmarshal?
		//mar.doMarshaling("person.xml", p);
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
	   // TODO did it unmarshal?
	//mar.marshal("person.xml", p);
    	
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
    	if (unsafe)
    		mar.setProcessExternalEntities(true);
    	ByteArrayInputStream bytes = new ByteArrayInputStream(person.getBytes());
    	
    	//nmarshal(new StreamSource(bytes));
		ret = p.getName();
	} catch (XmlMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   // TODO did it unmarshal?
	//mar.marshal("person.xml", p);
    	
    	model.addAttribute("name", ret);
        
        return "greeting";	// view template name
    }

}
