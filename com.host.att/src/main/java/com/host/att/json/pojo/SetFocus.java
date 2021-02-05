package com.host.att.json.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SetFocus{
    public String name;

	public String getName() {
		return name != null? name:"";
	}

	public void setName(String name) {
		this.name = name;
	}
}
