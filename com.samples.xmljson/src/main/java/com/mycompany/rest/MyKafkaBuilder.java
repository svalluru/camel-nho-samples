package com.mycompany.rest;

import org.apache.camel.builder.RouteBuilder;

public class MyKafkaBuilder extends RouteBuilder {

@Override
	public void configure() throws Exception {
        // TODO Auto-generated method stub
      //  from("kafka:test?brokers=localhost:9092")
  //  .log("Message received from Kafka : ${body}");

  from("kafka:my-topic?brokers=my-cluster-kafka-listener1-0-amq-streamss.apps.fuseocpsri.ocpdemo.net:443" +
             "&sslTruststoreLocation=/Users/svalluru/Desktop/amq_ocp.jks" +
             "&sslTruststorePassword=password" +
             "&seekTo=beginning" +
             "&securityProtocol=SSL")
        .to("stream:out");
  
		
	}
}