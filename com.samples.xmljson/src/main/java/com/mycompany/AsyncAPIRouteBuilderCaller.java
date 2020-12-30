package com.mycompany;

import org.apache.camel.builder.RouteBuilder;

public class AsyncAPIRouteBuilderCaller extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        from("direct:sec")
        .log("calling Direct VM...")
        .to("direct-vm:one");

        from("direct:async1")
        .log("calling VM...")
        .to("vm:async1Called");
    }
    
}
