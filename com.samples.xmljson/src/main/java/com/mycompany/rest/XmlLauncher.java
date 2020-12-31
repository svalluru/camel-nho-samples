package com.mycompany.rest;

import org.apache.camel.main.Main;

public class XmlLauncher {
    
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new XmlRouteBuilder());

        main.run(args);

    }
}
