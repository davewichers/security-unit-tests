package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	//ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringApplication.run(Application.class, args);
    }

}
