package com.host.att.json.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Object{
	
	@JsonProperty("com.demospace.intelligentinframanagement.rebootDM") 
    public RebootDM rebootDM;

	public RebootDM getRebootDM() {
		return rebootDM;
	}

	public void setRebootDM(RebootDM rebootDM) {
		this.rebootDM = rebootDM;
	}
}
