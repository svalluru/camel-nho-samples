package org.acme;
import java.util.Properties;

import org.apache.camel.Component;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;


public class TimerClass {
    
	public static void main(String[] args) throws Exception {
		//Main m = new Main();
		//m.addProperty("test", "abc123");
		DefaultCamelContext dc =  new DefaultCamelContext();
		//Properties pros =  new Properties();
		//pros.put("test", "value");
		//m.setInitialProperties(pros);
		
	PropertiesComponent propertiesComponent = new PropertiesComponent();
	//propertiesComponent.setInitialProperties(pros);
		propertiesComponent.setLocation("classpath:comp.properties");
		//dc.setPropertiesComponent(propertiesComponent);
		//dc.addComponent("properties", (Component) propertiesComponent);
		
		dc.addRoutes(new MyB());
		dc.start();
		
		//PropertiesComponent prop = m.getCamelContext().getComponent(PropertiesComponent.class); 
		//prop.setLocation("classpath:rider-test.properties");
				 
		//m.addRouteBuilder(new MyB());
		//m.getCamelContext().setPropertiesComponent(propertiesComponent);
		//m.run(args);

	}

}

