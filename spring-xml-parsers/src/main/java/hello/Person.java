package hello;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XmlRootElement(name="person")
@XStreamAlias("person")
public class Person {
	@XStreamImplicit(itemFieldName = "name")
	private String name;
	
	@XmlElement(name="name")  
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
} 
