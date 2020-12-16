package myfirst;

import java.util.Properties;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

public class MyB extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		Properties pros =  new Properties();
		pros.put("test", "value");
		
		PropertiesComponent pc = new PropertiesComponent();
		pc.setInitialProperties(pros);
		getContext().addComponent("properties", pc);
		
		from("timer:tc")
		.log("Hello"+ "{{test}}");
		System.out.println("config1");
	}

}
