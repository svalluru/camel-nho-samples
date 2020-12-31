package com.mycompany.rest;

import javax.activation.DataHandler;

import org.apache.camel.Attachment;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MPProc implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        DataHandler dataHandler = exchange.getIn().getBody(Attachment.class).getDataHandler();
        exchange.getIn().setHeader(Exchange.FILE_NAME, dataHandler.getName());
        exchange.getIn().setBody(dataHandler.getInputStream());
    }


}
