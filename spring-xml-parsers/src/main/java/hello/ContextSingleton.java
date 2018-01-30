package hello;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextSingleton {
	private static ContextSingleton instance = null;
	private ClassPathXmlApplicationContext ctx;
	public Object getBean(String type) {
		ctx.refresh();
		return ctx.getBean(type);
	}

	protected ContextSingleton() {
    	this.ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    	ctx.refresh();
	}
	
	public static ContextSingleton getInstance() {
		if (instance == null) {
			instance = new ContextSingleton();
		}
		return instance;
	}

}
