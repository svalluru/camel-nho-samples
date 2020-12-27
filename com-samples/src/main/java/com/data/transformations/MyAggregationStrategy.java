package com.data.transformations;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class MyAggregationStrategy implements AggregationStrategy {

    private String string;

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        string = oldExchange.getIn().getBody().toString();
        newExchange.getIn().setBody(string + " aggregation");
        return newExchange;

    }
    
}
