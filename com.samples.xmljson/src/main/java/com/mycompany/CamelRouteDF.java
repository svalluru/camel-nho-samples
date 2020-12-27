package com.mycompany;

import com.thoughtworks.xstream.XStream;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.model.dataformat.JaxbDataFormat;

public class CamelRouteDF extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
		JaxbDataFormat jaxb = new JaxbDataFormat();
	        jaxb.setContextPath("com.mycompany");

            from("file:/Users/svalluru/tmp?fileName=input.xml&noop=true")
			.log("picked file ? ")
            .unmarshal(jaxb)
            .process(new XMLProc())
	        .to("stream:out");

          XStream xst = new XStream();
            xst.alias("XStreamInput", XStreamInput.class);
            
            XStreamDataFormat xdf = new XStreamDataFormat(xst);
            
            from("file:/Users/svalluru/tmp?fileName=input2.xml&noop=true")
            .log("XStreamInput picked file ? ")
            .unmarshal(xdf)
            .marshal(xdf)
            //.process(new XMLProc())
	        .to("stream:out");

            from("file:/Users/svalluru/tmp?fileName=input3.xml&noop=true")
			.log("picked JAXB file ? ")
            .unmarshal()
            .jaxb("com.mycompany")
            .process(new XMLProc())
	        .to("stream:out");
            
    }
    
}
