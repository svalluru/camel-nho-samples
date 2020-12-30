package com.mycompany;

import org.apache.camel.builder.RouteBuilder;

public class AsyncAPIRouteBuilder extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        from("direct-vm:one")
        .log("Direct VM called")
        .to("stream:out");

        from("vm:async1Called")
        .log("Async VM called")
        .to("stream:out");

    }
    
}
