package com.mycompany;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

public class CamelRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		/* 
		 * You can define here the Camel Route.
		 * For instance, start by calling from() method, then use the Fluent API to build the Camel Route definition.
		 */
		JaxbDataFormat jaxb = new JaxbDataFormat();
	        jaxb.setContextPath("com.mycompany");

			from("file:/Users/svalluru/tmp?fileName=input1.xml&noop=true")
			.log("picked file ? ")
	        .unmarshal(jaxb)
	        .to("stream:out");
	}
}
