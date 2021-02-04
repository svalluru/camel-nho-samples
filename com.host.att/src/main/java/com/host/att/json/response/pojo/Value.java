package com.host.att.json.response.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Value{
    @JsonProperty("com.demospace.intelligentinframanagement.rebootDM") 
    public ComDemospaceIntelligentinframanagementRebootDM comDemospaceIntelligentinframanagementRebootDM;
    @JsonProperty("org.drools.core.common.DefaultFactHandle") 
    public OrgDroolsCoreCommonDefaultFactHandle orgDroolsCoreCommonDefaultFactHandle;
	public ComDemospaceIntelligentinframanagementRebootDM getComDemospaceIntelligentinframanagementRebootDM() {
		return comDemospaceIntelligentinframanagementRebootDM;
	}
	public void setComDemospaceIntelligentinframanagementRebootDM(
			ComDemospaceIntelligentinframanagementRebootDM comDemospaceIntelligentinframanagementRebootDM) {
		this.comDemospaceIntelligentinframanagementRebootDM = comDemospaceIntelligentinframanagementRebootDM;
	}
	public OrgDroolsCoreCommonDefaultFactHandle getOrgDroolsCoreCommonDefaultFactHandle() {
		return orgDroolsCoreCommonDefaultFactHandle;
	}
	public void setOrgDroolsCoreCommonDefaultFactHandle(
			OrgDroolsCoreCommonDefaultFactHandle orgDroolsCoreCommonDefaultFactHandle) {
		this.orgDroolsCoreCommonDefaultFactHandle = orgDroolsCoreCommonDefaultFactHandle;
	}
}
