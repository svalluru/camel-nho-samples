package com.mycompany.rest;

import java.io.InputStream;
import java.util.Map;

import org.apache.camel.Attachment;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class XmlRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("jetty://http://localhost:9984/test")
        .streamCaching()
        .process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                Message in = exchange.getIn();
                Map<String, Attachment> attachmentObjects = in.getAttachmentObjects();
                Attachment attachment = attachmentObjects.get("file_test");
                InputStream inputStream = attachment.getDataHandler().getDataSource().getInputStream();
                exchange.getIn().setBody(inputStream);
            }
        })
        . recipientList(constant("stream:out,validator:classpath:myxmlschema.xsd"));

        from("file:/Users/svalluru/tmp?fileName=sample_input.xml&noop=true")
        .log("read file")
        .split(xpath("/company/employee"), new MyOrderStrategy())//.xpath("/company/employee").aggregate(new MyOrderStrategy())
        .parallelProcessing()
        .wireTap("direct:wtap")
       .log("===========start============")
        .to("direct:processCompany")
        .log("===========end============")
        .end()
        .to("direct:print");

        from("direct:processCompany")
       // .throttle(1)
      //  .timePeriodMillis(3000)
       // .log(String.valueOf(System.currentTimeMillis()))
       .process(new Processor(){

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        // TODO Auto-generated method stub
                        String body = exchange.getIn().getBody(String.class);
                        Thread.sleep(5000);
                        exchange.getIn().setBody( body + "Adding!!!!!!!!!!!!!");

                    }
           
       })
        .to("stream:out");

        from("direct:print")
        .to("file:/Users/svalluru/tmp?fileName=sample_output.xml");

        from("direct:wtap")
        .log("wtap")
        .to("file:/Users/svalluru/tmp?fileName="+ Math.random() +".xml");
        //.to("stream:out");
    }
    
}
