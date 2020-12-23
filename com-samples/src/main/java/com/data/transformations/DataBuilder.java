package com.data.transformations;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class DataBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:callTransform")
        .transform(new Expression() {
            public <T> T evaluate(Exchange exchange, Class<T> type) {

                String s = exchange.getIn().getBody(String.class);
                return (T) (s != null ? s.concat("..Welcome to Red Hat") : null);
            }
        }).to("stream:out");

        from("direct:callProcessor")
            .process(new Processor() {

            public void process(Exchange exchange) throws Exception {
                String s = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody(s.concat("..Welcome to Red Hat"));
            }

        }).to("stream:out");

    }

}
