package com.mycompany.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CryptoDataFormat;

public class SecurityRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        CryptoDataFormat cryptoFormat = new CryptoDataFormat();
        cryptoFormat.setAlgorithm("DES");
        cryptoFormat.setKeyRef("mykey");

        from("direct:basic-encryption")
        .marshal(cryptoFormat).to("stream:out")
        .unmarshal(cryptoFormat)
                .to("stream:out");

        from("direct:callStatic")
        .log("random value " + Math.random())
        .to("stream:out");        
    }

}
