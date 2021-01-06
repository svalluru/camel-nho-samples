package com.mycompany.rest;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class MyOrderStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // TODO Auto-generated method stub
        
        if(oldExchange == null) return newExchange;

        String oldemp = oldExchange.getIn().getBody(String.class);
        String newemp = newExchange.getIn().getBody(String.class);
        System.out.println(Math.random());
        oldemp = oldemp + newemp;
        oldExchange.getIn().setBody(oldemp);

        return oldExchange;
    }
    
}
