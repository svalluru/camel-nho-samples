package org.acme;

import java.util.Properties;

import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.rest.RestBindingMode;

public class MyB extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration().component("camel-jetty").bindingMode(RestBindingMode.json);
		Properties pros =  new Properties();
		pros.put("test1", "_InitialProperty_");
		
		PropertiesComponent pc = new PropertiesComponent();
		pc.setInitialProperties(pros);
		pc.setLocation("classpath:comp.properties");
		getContext().setPropertiesComponent(pc);
		
		from("timer:tc")
		.log("Hello"+ "{{test1}}" + "{{test}}");
		
		from("rest:GET:hello?host=localhost:8976").transform().constant("Hello sri");
		
		System.out.println("config1");
	}

}
