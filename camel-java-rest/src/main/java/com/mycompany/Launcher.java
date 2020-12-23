package com.mycompany;

import org.apache.camel.main.Main;

public class Launcher {
    /**
     * A main() so we can easily run these routing rules issssn our IDEsssss
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
		      main.addRouteBuilder(new CamelRoute());
        main.run(args);
    }
}
