package myfirst;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class TimerClass {

	public static void main(String[] args) throws Exception {
		/*
		 * DefaultCamelContext dc = new DefaultCamelContext(); dc.addRoutes(new MyB());
		 * dc.start();
		 */

		
		Main m = new Main();

	//	CamelContext camelContext = m.getOrCreateCamelContext();
		//camelContext.addComponent("properties", PropertiesComponent.class);
		
		//PropertiesComponent propertiesComponent = (PropertiesComponent) camelContext.getComponent(PropertiesComponent.class.getName());
		//propertiesComponent.setLocation("classpath:comp.properties");
	
		m.addRouteBuilder(new MyB());
		m.run(args);

	}

}
