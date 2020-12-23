package myfirst;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ValueBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.camel.support.ExpressionAdapter;

public class TransformUsingBodyFunctionsExample {
    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        try {
            camelContext.addRoutes(new RouteBuilder() {
                public void configure() {
                    from("direct:bodyPrepend").transform(body().prepend("Hello")).to("stream:out");
                    from("direct:bodyConvert").transform(body().convertToString()).to("stream:out");
                    from("direct:valueBuilder").transform(new ValueBuilder(new ExpressionAdapter(){
                        @Override
                        public Object evaluate(Exchange exchange) {
                            String s = exchange.getIn().getBody(String.class);
                            return s != null ? s.replace('\n', ' ') : null;
                        }
                    })).to("stream:out");
                }
            });
            camelContext.start();
            ProducerTemplate template = new DefaultProducerTemplate(
                    camelContext);
            template.start();
            template.sendBody("direct:bodyPrepend", "World");
            template.sendBody("direct:bodyConvert", new ValueObject("HelloWorld"));
            template.sendBody("direct:valueBuilder", "Hello\nWorld");
        } finally {
            //camelContext.stop();
        }
    }
     
    private static class ValueObject {
        private String s;
        ValueObject(String s) {
            this.s = s;
        }
        public String toString() {
            return "Value(" + s + ")";
        }
    }
}



