package com.mycompany;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestOperationResponseMsgDefinition;

public class CamelRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//from("timer:tc")
		//.log("Hello");
		RestOperationResponseMsgDefinition res = new RestOperationResponseMsgDefinition();
		res.setCode("201");
		res.setMessage("custom response");
		res.message("hello");

		restConfiguration().component("jetty").host("localhost").port(8976)
		.bindingMode(RestBindingMode.auto)
		.apiContextPath("/api-doc")
		.apiProperty("api.property", "User API")
		.apiProperty("cors", "true");
		
		rest("/sayHello")
		.get().consumes("application/json")
		.responseMessage(res).toD("direct:${header.h1}"); // dynamic to
		

		from("rest:GET:hello").transform().constant("Hello sri");

		from("direct:ac")
		.log("test").setBody().simple("hello test");;
		
		rest("/users/")
	    .post().type(UserPojo.class)
	        .to("direct:newUser");
		
		from("direct:newUser")
		.marshal().json(JsonLibrary.Jackson)
		.log("test"+"${body}")
		.transform().constant("Welcome");
		
		rest("/users/")
	    .post("lives").type(UserPojo.class)
	        .route()
	            .choice()
	                .when().simple("${body.id} < 100")
	                    .bean(new UserErrorService(), "idToLowError")
	                .otherwise()
	                    .bean(new UserService(), "livesWhere");
		
		System.out.println("config1");
		
		from("timer:foo?period=1000")
		.setBody(constant("Hello World"))
		.to("stream:out");
		
	}
}
