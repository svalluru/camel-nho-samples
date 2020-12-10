package com.mycompany;

import org.apache.camel.Exchange;

public class UserService {
	
	 public void livesWhere(Exchange exchange) {
	        exchange.getIn().setBody("id value is too high");
	        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain");
	        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
	    }

}
