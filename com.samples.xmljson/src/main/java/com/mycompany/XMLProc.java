package com.mycompany;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class XMLProc implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Employee employee = exchange.getIn().getBody(Employee.class);
		employee.setCompany("A RedHat");
		exchange.getIn().setBody(employee);

    }
    
}
