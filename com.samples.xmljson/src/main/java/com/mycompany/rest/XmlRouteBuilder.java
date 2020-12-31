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




    }
    
}
