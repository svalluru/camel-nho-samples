package com.host.att.json.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class FireAllRules{
    @JsonProperty("out-identifier") 
    public String outIdentifier;

	public String getOutIdentifier() {
		return outIdentifier;
	}

	public void setOutIdentifier(String outIdentifier) {
		this.outIdentifier = outIdentifier;
	}
}
