package com.mycompany.rest;

import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

public class SecurityLauncher {


    
    public static void main(String... args) throws Exception {
        SimpleRegistry registry =  new SimpleRegistry();
        registry.put("mykey", KeyGenerator.getInstance("DES").generateKey());
        CamelContext ctx =  new DefaultCamelContext(registry);
        ctx.addRoutes(new SecurityRouteBuilder());
        ctx.start();

        ProducerTemplate pt = ctx.createProducerTemplate();
        pt.requestBody("direct:basic-encryption", "hello Direct VM");

        TimeUnit.MINUTES.sleep(5);

    }
    
}
