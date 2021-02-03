package com.mycompany.rest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.model.rest.RestBindingMode;

public class JDBCCompBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        restConfiguration().component("jetty").host("localhost").port("9987").bindingMode(RestBindingMode.json);

        rest("/host").post("/reboot")
        .type(DMInput.class)
        .outType(DMOutput.class)
        .consumes("application/json").produces("application/json").to("direct:checkReboot");

        from("direct:checkReboot")
        .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                //HttpMessage msg = (HttpMessage) exchange.getIn().getBody(DMInput.class);
                DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
                //String str = msg.getRequest().getParameter("host");
               // DMInput dmIn = (DMInput) msg.getBody(DMInput.class);
                String sqlstr = "select * from Reboot where Host='"+dmIn.getHosts()[0].getHost()+"'";
                //exchange.setProperty("HOST_VALUE", sqlstr);
                exchange.getIn().setBody(sqlstr);
			}
        })
        //.setBody(constant(exchangeProperty("HOST_VALUE")))
        .to("jdbc:mysqldb")
        .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                ProducerTemplate template = exchange.getContext().createProducerTemplate();

                List<HashMap<String, Object>> dbResult= (List<HashMap<String, Object>>) exchange.getIn().getBody();
                
                for (HashMap<String,Object> hashMap : dbResult) {
                        Timestamp lastRebootVal = (Timestamp) hashMap.get("LastRebootTime");
                        String host = (String) hashMap.get("Host");
                        int restartCount = (int) hashMap.get("RestartCount");
                        boolean restart =  (boolean) hashMap.get("Restart");
                        boolean alertOps =  (boolean) hashMap.get("AlertOps");

                        System.out.println(lastRebootVal);

                        //Future<Object> future = template.asyncRequestBody("jetty://http://0.0.0.0/myservice?rebootTime="+lastRebootVal, "hello");
                        //String response = template.extractFutureBody(future, String.class);
                        boolean restartRequired = true;
                        if(restartRequired){
                            DMOutput out = new DMOutput();
                            out.setHost(host);
                            out.setRestartCount(restartCount++);
                            out.setTimeStamp(String.valueOf(System.currentTimeMillis()));
                                 
                        }else{
                            DMOutput dmOut = new DMOutput();
                            dmOut.setHost(host);
                            dmOut.setRestart(false);
                            dmOut.setAlertOps(true);

                            exchange.getIn().setBody(dmOut);
                        }
                      //  template.asyncSendBody("direct:callDMAPI", lastRebootVal);
                }
                
			}
        });

        from("direct:updateRestart")
        .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                //HttpMessage msg = (HttpMessage) exchange.getIn().getBody(DMInput.class);
                DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
                //String str = msg.getRequest().getParameter("host");
               // DMInput dmIn = (DMInput) msg.getBody(DMInput.class);
                String sqlstr = "update Reboot Set Host ='"+dmIn.getHosts()[0].getHost()+"' where Host='"+dmIn.getHosts()[0].getHost()+"'";
                //exchange.setProperty("HOST_VALUE", sqlstr);
                exchange.getIn().setBody(sqlstr);
			}
        })
        //.setBody(constant(exchangeProperty("HOST_VALUE")))
        .to("jdbc:mysqldb");





        rest("/host").put("/reboot")
        .type(DMInput.class)
        .outType(DMOutput.class)
        .consumes("application/json").produces("application/json").to("direct:updateReboot");

        from("direct:updateReboot")
        .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                //HttpMessage msg = (HttpMessage) exchange.getIn().getBody(DMInput.class);
                DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
                //String str = msg.getRequest().getParameter("host");
               // DMInput dmIn = (DMInput) msg.getBody(DMInput.class);
                String sqlstr = "update Reboot Set Host ='"+dmIn.getHosts()[0].getHost()+"' where Host='"+dmIn.getHosts()[0].getHost()+"'";
                //exchange.setProperty("HOST_VALUE", sqlstr);
                exchange.getIn().setBody(sqlstr);
			}
        })
        //.setBody(constant(exchangeProperty("HOST_VALUE")))
        .to("jdbc:mysqldb");

        rest("/host").delete("/reboot")
        .type(DMInput.class)
        .outType(DMOutput.class)
        .consumes("application/json").produces("application/json").to("direct:deleteReboot");

        from("direct:deleteReboot")
        .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                //HttpMessage msg = (HttpMessage) exchange.getIn().getBody(DMInput.class);
                DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
                //String str = msg.getRequest().getParameter("host");
               // DMInput dmIn = (DMInput) msg.getBody(DMInput.class);
                String sqlstr = "delete from Reboot where Host='"+dmIn.getHosts()[0].getHost()+"'";
                //exchange.setProperty("HOST_VALUE", sqlstr);
                exchange.getIn().setBody(sqlstr);
			}
        })
        //.setBody(constant(exchangeProperty("HOST_VALUE")))
        .to("jdbc:mysqldb");


        rest("/host").post()
        .type(DMInput.class)
        .outType(DMOutput.class)
        .consumes("application/json").produces("application/json").to("direct:createReboot");

        from("direct:createReboot")
        .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                //HttpMessage msg = (HttpMessage) exchange.getIn().getBody(DMInput.class);
                DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
                //String str = msg.getRequest().getParameter("host");
               // DMInput dmIn = (DMInput) msg.getBody(DMInput.class);
                String sqlstr = "insert into Reboot values('"+dmIn.getHosts()[0].getHost()+"','"+dmIn.getHosts()[0].getHost()+"');";
                //exchange.setProperty("HOST_VALUE", sqlstr);
                exchange.getIn().setBody(sqlstr);
			}
        })
        //.setBody(constant(exchangeProperty("HOST_VALUE")))
        .to("jdbc:mysqldb");
       // from("direct:callDMAPI")
       // .setHeader(Exchange.HTTP_QUERY, simple("userid=${body}"))
        //.toD("jetty://http://myloginserver:8080/login");
        







/* 
        from("timer://foo")
        .setBody(constant("select * from HostReboot"))
        .to("jdbc:mysqldb")
        .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                Object body = exchange.getIn().getBody();
                System.out.println(body);
			}
        });


        from("timer://insertdb?repeatCount=1")
        .setBody(constant("delete from HostReboot where Host='h1.com'"))
        .to("jdbc:mysqldb");

        from("timer://insertdb?repeatCount=1")
        .setBody(constant("insert into HostReboot values('h1.com','m5.Large','2016-11-16 06:43:19.77')"))
        .to("jdbc:mysqldb"); */

        

    }
    
}
