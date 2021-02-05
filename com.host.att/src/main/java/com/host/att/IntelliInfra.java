package com.host.att;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.commons.dbcp.BasicDataSource;

public class IntelliInfra {
    public static void main(String... args) throws Exception {


        BasicDataSource ds =  new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/iim");
        ds.setUsername("iimuser");
        ds.setPassword("Iimuser@123");

        SimpleRegistry registry =  new SimpleRegistry();
        registry.put("mysqldb", ds);
        CamelContext ctx =  new DefaultCamelContext(registry);
        ctx.addRoutes(new IntelliInfraBuilder());
        //ctx.setTracing(true);
        ctx.start();
      
        TimeUnit.MINUTES.sleep(5);
    }
}
