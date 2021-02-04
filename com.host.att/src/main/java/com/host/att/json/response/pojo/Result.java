package com.host.att.json.response.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result{
    @JsonProperty("execution-results") 
    public ExecutionResults executionResults;

	public ExecutionResults getExecutionResults() {
		return executionResults;
	}

	public void setExecutionResults(ExecutionResults executionResults) {
		this.executionResults = executionResults;
	}
}
