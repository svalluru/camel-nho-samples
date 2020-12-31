package com.mycompany.rest;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Attachment;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestOperationResponseMsgDefinition;

public class RestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        restConfiguration().component("jetty").host("localhost").port("9987").bindingMode(RestBindingMode.json);

        rest("/upload").consumes("multipart/form-data").produces("application/json").post("/report")
                .to("direct:upload");

        rest("/info").get().consumes("application/json").to("direct:ac");

        from("direct:ac").log("test").setBody().simple("hello test");

        from("direct:upload").unmarshal().mimeMultipart().split().attachments().process(new MPProc())
                .to("file:/Users/svalluru/tmp");

        getContext().getGlobalOptions().put("CamelJettyTempDir", "target");

        from("jetty://http://localhost:9988/test").process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                Message in = exchange.getIn();
                System.out.println(in.hasAttachments());
                Map<String, Attachment> attachmentObjects = in.getAttachmentObjects();
                Attachment attachment = attachmentObjects.get("file_test");
                System.out.println(attachment.getDataHandler().getDataSource().getContentType());
                InputStream inputStream = attachment.getDataHandler().getDataSource().getInputStream();
                exchange.getIn().setBody(inputStream);
            }
        }).to("stream:out");
    }

}
