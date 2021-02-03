package com.mycompany.rest;

import java.io.InputStream;
import java.util.Map;

import org.apache.camel.Attachment;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class RestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        restConfiguration().component("jetty").host("localhost").port("9987").bindingMode(RestBindingMode.auto).endpointProperty("attachmentMultipartBinding", "true")
        .apiContextPath("/api-doc").apiProperty("api.title", "Upload Details").apiProperty("cors", "true");

        rest("/upload").consumes("multipart/form-data").post("/report")
                .description("Upload file")
                .responseMessage().code(200).responseModel(String.class).endResponseMessage()
                .responseMessage().code(500).responseModel(String.class).endResponseMessage()
                .to("direct:upload");

        rest("/info").get()
        .consumes("application/json").to("direct:ac");

        from("direct:ac").log("test")
        .setBody().simple("hello test");

        from("direct:upload")
       // .unmarshal().json(JsonLibrary.Fastjson)
        .process(new MPProc())
                .to("file:/Users/svalluru/tmp");

        getContext().getGlobalOptions().put("CamelJettyTempDir", "target");

        from("jetty://http://localhost:9988/test").process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                Message in = exchange.getIn();
                Map<String, Attachment> attachmentObjects = in.getAttachmentObjects();
                Attachment attachment = attachmentObjects.get("file_test");
                InputStream inputStream = attachment.getDataHandler().getDataSource().getInputStream();
                exchange.getIn().setBody(inputStream);
            }
        })
        //.to("json-validator:classpath:json_schema.json");
        .to("stream:out");
    }

}
