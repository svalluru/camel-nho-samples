package com.host.att;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.host.att.json.pojo.Command;
import com.host.att.json.pojo.Insert;
import com.host.att.json.pojo.RebootDM;
import com.host.att.json.pojo.Root;

public class RESTBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("timer:tc")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				
				RebootDM rdm =  new RebootDM();
				rdm.setHost("h1.com");
				rdm.setAlertOps("string");
				rdm.setRestart("false");
				rdm.setRestartCount(1);
				rdm.setRestartedOn(0);

				
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
				
				ObjectMapper om = new ObjectMapper();
				String writeValueAsString = om.writeValueAsString(r);
				Root readValue = om.readValue(writeValueAsString, Root.class);
				System.out.println(readValue);
				
				System.out.println();
				
				
				
				
			}
		});
		
		
	}

}
