package com.host.att.json.pojo;

public class RebootDM{
    public String host;
    public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getRestart() {
		return restart;
	}
	public void setRestart(String restart) {
		this.restart = restart;
	}
	public int getRestartedOn() {
		return restartedOn;
	}
	public void setRestartedOn(int restartedOn) {
		this.restartedOn = restartedOn;
	}
	public int getRestartCount() {
		return restartCount;
	}
	public void setRestartCount(int restartCount) {
		this.restartCount = restartCount;
	}
	public String getAlertOps() {
		return alertOps;
	}
	public void setAlertOps(String alertOps) {
		this.alertOps = alertOps;
	}
	public String restart;
    public int restartedOn;
    public int restartCount;
    public String alertOps;
}
