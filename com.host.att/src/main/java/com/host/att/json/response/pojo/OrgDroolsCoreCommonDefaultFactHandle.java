package com.host.att.json.response.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrgDroolsCoreCommonDefaultFactHandle{
    @JsonProperty("external-form") 
    public String externalForm;

	public String getExternalForm() {
		return externalForm;
	}

	public void setExternalForm(String externalForm) {
		this.externalForm = externalForm;
	}
}
