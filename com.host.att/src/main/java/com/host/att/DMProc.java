package com.host.att;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DMProc implements Processor {

    private Object body;

    @Override
    public void process(Exchange exchange) throws Exception {
        // TODO Auto-generated method stub
        exchange.getIn().setBody(this.getBody());
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
    
}
