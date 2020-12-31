package com.mycompany.rest;

import org.apache.camel.main.Main;

public class RestLauncher {
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new RestRouteBuilder());

        main.run(args);

    }
}