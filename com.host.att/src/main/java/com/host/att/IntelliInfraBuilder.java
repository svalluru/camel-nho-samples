package com.host.att;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.host.att.json.pojo.FireAllRules;
import com.host.att.json.pojo.Insert;
import com.host.att.json.pojo.RebootDM;
import com.host.att.json.pojo.Root;
import com.host.att.json.pojo.SetFocus;
import com.host.att.json.response.pojo.ComDemospaceIntelligentinframanagementRebootDM;
import com.host.att.json.response.pojo.Result;

public class IntelliInfraBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("jetty").host("0.0.0.0").port("8181").bindingMode(RestBindingMode.json);

		rest("/host").post("/reboot")
		.type(DMInput.class)
		.outType(DMOutput.class)
		.consumes("application/json").produces("application/json")
		.to("direct:processHosts");


		from("direct:processHosts")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
				Hosts[] hosts = dmIn.getHosts();
				ProducerTemplate template = exchange.getContext().createProducerTemplate();
				List<DMOutput> dmList =  new ArrayList<DMOutput>();


				for (Hosts host : hosts) {
					String sqlstr = "select * from reboot where Host='"+host.getHost()+"'";
					Object requestBody =  template.requestBody("direct:callJDBC", sqlstr);

					ExchangeBuilder anExchange = ExchangeBuilder.anExchange(exchange.getContext());
					DMProc dm =  new DMProc();
					dm.setBody(requestBody);
					dm.process(anExchange.build());
					System.setProperty("HOST_VALUE", host.getHost());
					Exchange request = template.request("direct:checkReboot", dm);
					dmList.add((DMOutput) request.getIn().getBody());  
					System.out.println(request);
				}
				exchange.getIn().setBody(dmList);

			}
		});


		from("direct:callJDBC")

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
					out.setRestart(true);
					out.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());

					template.sendBody("direct:newReboot", out);
					exchange.getIn().setBody(out);
				}

				for (HashMap<String,Object> hashMap : dbResult) {
					Timestamp lastRebootVal = (Timestamp) hashMap.get("LastRebootTime");
					String host = (String) hashMap.get("host");
					int restartCount = (int) hashMap.get("restartCount");

					System.out.println(lastRebootVal);

					Root dmObject = getDMObject(host,restartCount,lastRebootVal, "rebootDM", "reboot", "firedActivations");
					//exchange.getIn().setHeader("Authorization", constant("Basic cGFtQWRtaW46cmVkaGF0cGFtMSE="));
					//exchange.getIn().setHeader("Content-Type", constant("application/json"));		
					String reqJson = new ObjectMapper().writeValueAsString(dmObject);
					System.out.println(reqJson);

					Map< String, Object> headers= new HashMap<String,Object>();
					headers.put(Exchange.HTTP_METHOD, "POST");
					headers.put("Content-Type", "application/json");
					headers.put("Authorization", "Basic cGFtQWRtaW46cmVkaGF0cGFtMSE=");
					headers.put("Accept", "application/json");

					//Future<Object> future = template.asyncRequestBodyAndHeaders("jetty://http://0.0.0.0:8080/kie-server/services/rest/server/containers/instances/IntelligentInfraManagement_1.0.0-SNAPSHOT", reqJson, headers);
					//com.host.att.json.response.pojo.Root response = template.extractFutureBody(future, com.host.att.json.response.pojo.Root.class);

					byte[] future = (byte[]) template.requestBodyAndHeaders("jetty://http://0.0.0.0:8080/kie-server/services/rest/server/containers/instances/IntelligentInfraManagement_1.0.0-SNAPSHOT", reqJson, headers);
					
					//ObjectMapper om = new ObjectMapper();
					ObjectInputStream in =new ObjectInputStream(new ByteArrayInputStream(future));
					System.out.println(in.readObject());
					
					//com.host.att.json.response.pojo.Root response = (com.host.att.json.response.pojo.Root) future;
					com.host.att.json.response.pojo.Root response = (com.host.att.json.response.pojo.Root) in.readObject();
					//System.out.println(response);
					//com.host.att.json.response.pojo.Root parseDMReturnValue = parseDMReturnValue(response);
					
					//TODO add null check and fix accessing array
					//ComDemospaceIntelligentinframanagementRebootDM rebootDM = parseDMReturnValue.getResult().getExecutionResults().getResults().get(0).getValue().getComDemospaceIntelligentinframanagementRebootDM();
					ComDemospaceIntelligentinframanagementRebootDM rebootDM = response.getResult().getExecutionResults().getResults().get(0).getValue().getComDemospaceIntelligentinframanagementRebootDM();
					
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

/*			private com.host.att.json.response.pojo.Root parseDMReturnValue(Root response) {
				
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

			}*/

			private Root getDMObject(String host, int restartCount, Timestamp lastRebootVal, String out_id_in, String rule_flow, String out_id) {

				RebootDM rdm =  new RebootDM();
				rdm.setHost(host);
				rdm.setRestartCount(restartCount);
				long milliseconds = new Timestamp(System.currentTimeMillis()).getTime() - lastRebootVal.getTime();
				int seconds = (int) milliseconds / 1000;
				int minutes = (seconds % 3600) / 60;
				rdm.setRestartedMin(minutes);


				com.host.att.json.pojo.Object obj =  new com.host.att.json.pojo.Object();
				obj.setRebootDM(rdm);

				Insert ins =  new Insert();
				ins.setObject(obj);
				ins.setOutIdentifier(out_id_in);

				List<Command> cmdList =  new ArrayList<Command>();
				Command cmd = new Command();
				cmd.setInsert(ins);
				
				Command cmdSetFocus = new Command();
				SetFocus sf = new SetFocus();
				sf.setName(rule_flow);
				cmdSetFocus.setSetFocus(sf);
				
				Command cmdfireAll = new Command();
				FireAllRules faR = new FireAllRules();
				faR.setOutIdentifier(out_id);
				cmdfireAll.setFireAllRules(faR);
				
				cmdList.add(cmd);
				cmdList.add(cmdSetFocus);
				cmdList.add(cmdfireAll);


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
				String sqlstr = "insert into reboot values('"+dmOut.getHost()+"',"+dmOut.getRestartCount()+",'"+dmOut.getTimeStamp()+"',"+dmOut.isAlertOps()+","+ dmOut.isRestart()+");";
				System.out.println(sqlstr);
				exchange.getIn().setBody(sqlstr);
			}
		})
		.to("jdbc:mysqldb");

		from("direct:updateRestart")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
				String sqlstr = "update reboot Set Host ='"+dmIn.getHosts()[0].getHost()+"' where Host='"+dmIn.getHosts()[0].getHost()+"'";
				exchange.getIn().setBody(sqlstr);
			}
		})
		.to("jdbc:mysqldb");





		rest("/host").put("/reboot")
		.type(DMInput.class)
		.outType(DMOutput.class)
		.consumes("application/json").produces("application/json").to("direct:updateReboot");

		from("direct:updateReboot")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
				String sqlstr = "update reboot Set Host ='"+dmIn.getHosts()[0].getHost()+"' where Host='"+dmIn.getHosts()[0].getHost()+"'";
				exchange.getIn().setBody(sqlstr);
			}
		})
		.to("jdbc:mysqldb");

		rest("/host").delete("/reboot")
		.type(DMInput.class)
		.outType(DMOutput.class)
		.consumes("application/json").produces("application/json").to("direct:deleteReboot");

		from("direct:deleteReboot")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
				String sqlstr = "delete from reboot where Host='"+dmIn.getHosts()[0].getHost()+"'";
				exchange.getIn().setBody(sqlstr);
			}
		})
		.to("jdbc:mysqldb");


		rest("/host").post()
		.type(DMInput.class)
		.outType(DMOutput.class)
		.consumes("application/json").produces("application/json").to("direct:createReboot");

		from("direct:createReboot")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				DMInput dmIn = (DMInput) exchange.getIn().getBody(DMInput.class);
				String sqlstr = "insert into Reboot values('"+dmIn.getHosts()[0].getHost()+"','"+dmIn.getHosts()[0].getHost()+"');";
				exchange.getIn().setBody(sqlstr);
			}
		})
		.to("jdbc:mysqldb");

	}

}
