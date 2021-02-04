package com.host.att.json.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Command{
	
    public Insert insert;
	@JsonProperty("set-focus") 
	public SetFocus setFocus;
	@JsonProperty("fire-all-rules") 
    public FireAllRules fireAllRules;

    public Insert getInsert() {
		return insert;
	}
	public void setInsert(Insert insert) {
		this.insert = insert;
	}
	public SetFocus getSetFocus() {
		return setFocus;
	}
	public void setSetFocus(SetFocus setFocus) {
		this.setFocus = setFocus;
	}
	public FireAllRules getFireAllRules() {
		return fireAllRules;
	}
	public void setFireAllRules(FireAllRules fireAllRules) {
		this.fireAllRules = fireAllRules;
	}

}
