package com.mycompany;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

public class AsyncAPILauncher {
    private static ProducerTemplate pt;

	public static void main(String... args) throws Exception {
        CamelContext ctx1 =  new DefaultCamelContext();
        ctx1.addRoutes(new AsyncAPIRouteBuilderCaller());
        ctx1.start();

      CamelContext ctx =  new DefaultCamelContext();
        ctx.addRoutes(new AsyncAPIRouteBuilder());
        ctx.start();

        pt = ctx1.createProducerTemplate();
        pt.requestBody("direct:sec", "hello Direct VM");
        pt.requestBody("direct:async1", "hello VM");


        TimeUnit.MINUTES.sleep(5);
    }
}
