package com.host.att;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.model.rest.RestBindingMode;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.host.att.json.pojo.Command;
import com.host.att.json.pojo.Insert;
import com.host.att.json.pojo.RebootDM;
import com.host.att.json.pojo.Root;
import com.host.att.json.response.pojo.ComDemospaceIntelligentinframanagementRebootDM;
import com.host.att.json.response.pojo.Result;

public class JDBCCompBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("jetty").host("localhost").port("9987").bindingMode(RestBindingMode.json);

		rest("/host").post("/reboot")
		.type(DMInput.class)
		.outType(DMOutput.class)
		.consumes("application/json").produces("application/json")
		//.to("direct:checkReboot");
		.to("direct:processHosts");


		from("direct:processHosts")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				//HttpMessage msg = (HttpMessage) exchange.getIn().getBody(DMInput.class);
				DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
				Hosts[] hosts = dmIn.getHosts();
				ProducerTemplate template = exchange.getContext().createProducerTemplate();
				List<DMOutput> dmList =  new ArrayList<DMOutput>();


				for (Hosts host : hosts) {
					String sqlstr = "select * from Reboot where Host='"+host.getHost()+"'";
					//exchange.getIn().setBody(sqlstr);
					Object requestBody =  template.requestBody("direct:callJDBC", sqlstr);

					//  DMOutput dmOut =  (DMOutput) template.requestBody("direct:checkReboot", requestBody);

					ExchangeBuilder anExchange = ExchangeBuilder.anExchange(exchange.getContext());
					//anExchange.withBody(requestBody);
					DMProc dm =  new DMProc();
					dm.setBody(requestBody);
					dm.process(anExchange.build());
					Exchange request = template.request("direct:checkReboot", dm);
					dmList.add((DMOutput) request.getIn().getBody());  
					System.out.println(request);
				}
				exchange.getIn().setBody(dmList);
				//String str = msg.getRequest().getParameter("host");
				// DMInput dmIn = (DMInput) msg.getBody(DMInput.class);
			}
		});


		from("direct:callJDBC")
		/*     .process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
                //HttpMessage msg = (HttpMessage) exchange.getIn().getBody(DMInput.class);
                DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
                //String str = msg.getRequest().getParameter("host");
               // DMInput dmIn = (DMInput) msg.getBody(DMInput.class);
                String sqlstr = "select * from Reboot where Host='"+dmIn.getHosts()[0].getHost()+"'";
                System.setProperty("HOST_VALUE", dmIn.getHosts()[0].getHost());
                exchange.getIn().setBody(sqlstr);
			}
        })*/
		//.setBody(constant(exchangeProperty("HOST_VALUE")))
		.to("jdbc:mysqldb");

		from("direct:checkReboot")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				ProducerTemplate template = exchange.getContext().createProducerTemplate();

				List<HashMap<String, Object>> dbResult= (List<HashMap<String, Object>>) exchange.getIn().getBody();

				if(dbResult != null && dbResult.size() == 0){
					DMOutput out = new DMOutput();
					out.setHost(System.getProperty("HOST_VALUE"));
					out.setRestartCount(1);
					out.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
					template.sendBody("direct:newReboot", out);
					exchange.getIn().setBody(out);
				}

				for (HashMap<String,Object> hashMap : dbResult) {
					Timestamp lastRebootVal = (Timestamp) hashMap.get("LastRebootTime");
					String host = (String) hashMap.get("Host");
					int restartCount = (int) hashMap.get("RestartCount");
					//boolean restart =  (boolean) hashMap.get("Restart");
					//boolean alertOps =  (boolean) hashMap.get("AlertOps");

					System.out.println(lastRebootVal);

					Root dmObject = getDMObject(host,restartCount,lastRebootVal);

					Future<Object> future = template.asyncRequestBody("jetty://http://0.0.0.0/myservice", dmObject);
					String response = template.extractFutureBody(future, String.class);
					com.host.att.json.response.pojo.Root parseDMReturnValue = parseDMReturnValue(response);
					
					//TODO add null check and fix accessing array
					ComDemospaceIntelligentinframanagementRebootDM rebootDM = parseDMReturnValue.getResult().getExecutionResults().getResults().get(0).getValue().getComDemospaceIntelligentinframanagementRebootDM();
					
					
					if(Boolean.valueOf(rebootDM.getRestart())){
						DMOutput out = new DMOutput();
						out.setHost(host);
						out.setRestartCount(restartCount++);
						out.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
						exchange.getIn().setBody(out);
						//template.asyncSendBody("direct:updateDB", out);
					}else{
						DMOutput dmOut = new DMOutput();
						dmOut.setHost(host);
						dmOut.setRestart(Boolean.valueOf(rebootDM.getRestart()));
						dmOut.setAlertOps(Boolean.valueOf(rebootDM.getAlertOps()));
						exchange.getIn().setBody(dmOut);
					}
				}

			}

			private com.host.att.json.response.pojo.Root parseDMReturnValue(String response) {
				
				ObjectMapper om = new ObjectMapper();
				try {
					return om.readValue(response, com.host.att.json.response.pojo.Root.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;

			}

			private Root getDMObject(String host, int restartCount, Timestamp lastRebootVal) {

				RebootDM rdm =  new RebootDM();
				rdm.setHost(host);
				//rdm.setAlertOps("string");
				//rdm.setRestart("false");
				rdm.setRestartCount(restartCount);
				rdm.setRestartedOn(5);


				com.host.att.json.pojo.Object obj =  new com.host.att.json.pojo.Object();
				obj.setRebootDM(rdm);

				Insert ins =  new Insert();
				ins.setObject(obj);

				List<Command> cmdList =  new ArrayList<Command>();
				Command cmd = new Command();
				cmd.setInsert(ins);
				cmdList.add(cmd);

				Root r = new Root();
				r.setCommands(cmdList);
				return r;
			}
		});

		from("direct:newReboot")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				DMOutput dmOut = (DMOutput) exchange.getIn().getBody(DMOutput.class);
				String sqlstr = "insert into Reboot values('"+dmOut.getHost()+"',"+dmOut.getRestartCount()+",'"+dmOut.getTimeStamp()+"',"+dmOut.isAlertOps()+","+ dmOut.isRestart()+");";
				System.out.println(sqlstr);
				exchange.getIn().setBody(sqlstr);
			}
		})
		.to("jdbc:mysqldb");

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
