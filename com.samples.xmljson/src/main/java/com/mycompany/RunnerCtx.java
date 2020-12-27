package com.mycompany;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class RunnerCtx {
      /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        CamelContext ctx =  new DefaultCamelContext();
        ctx.getTypeConverterRegistry().addTypeConverter(To.class, String.class, new B2STypeConverter());
        ctx.addRoutes(new CamelRouteDF());

        ctx.start();
        TimeUnit.MINUTES.sleep(5);
    }
}
