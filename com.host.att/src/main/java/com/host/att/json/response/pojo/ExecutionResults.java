package com.host.att.json.response.pojo;

import java.util.List;

public class ExecutionResults{
    public List<Results> results;
    public List<Results> getResults() {
		return results;
	}
	public void setResults(List<Results> results) {
		this.results = results;
	}
	public List<Fact> facts;

    public List<Fact> getFacts() {
		return facts;
	}
	public void setFacts(List<Fact> facts) {
		this.facts = facts;
	}
}
