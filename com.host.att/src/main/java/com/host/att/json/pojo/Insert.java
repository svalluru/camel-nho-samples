package com.host.att.json.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Insert{
    public Object object;
	@JsonProperty("out-identifier") 
	public String outIdentifier;

    public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getOutIdentifier() {
		return outIdentifier;
	}
	public void setOutIdentifier(String outIdentifier) {
		this.outIdentifier = outIdentifier;
	}
}
