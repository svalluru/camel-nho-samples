package org.acme;

import java.util.Properties;

import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

public class MyB extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		Properties pros =  new Properties();
		pros.put("test1", "_InitialProperty_");
		
		PropertiesComponent pc = new PropertiesComponent();
		pc.setInitialProperties(pros);
		pc.setLocation("classpath:comp.properties");
		getContext().setPropertiesComponent(pc);
		
		from("timer:tc")
		.log("Hello"+ "{{test1}}" + "{{test}}");
		System.out.println("config1");
	}

}
