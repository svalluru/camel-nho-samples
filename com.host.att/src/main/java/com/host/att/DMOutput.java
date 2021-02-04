package com.host.att;

public class DMOutput {
    private String host;
    private boolean restart;
    private int restartCount;
    private boolean alertOps;
    private String timeStamp;

    public String getHost() {
        return host;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isAlertOps() {
        return alertOps;
    }

    public void setAlertOps(boolean alertOps) {
        this.alertOps = alertOps;
    }

    public int getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public void setHost(String host) {
        this.host = host;
    }

    

}
