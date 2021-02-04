package com.host.att;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.commons.dbcp.BasicDataSource;

public class RESTLauncher {
    public static void main(String... args) throws Exception {


        CamelContext ctx =  new DefaultCamelContext();
        ctx.addRoutes(new RESTBuilder());
        ctx.start();

        TimeUnit.MINUTES.sleep(5);

    }
}
