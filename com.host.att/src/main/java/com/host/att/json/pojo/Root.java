package com.host.att.json.pojo;

import java.util.List;

public class Root{
    public Object lookup;
    public Object getLookup() {
		return lookup;
	}
	public void setLookup(Object lookup) {
		this.lookup = lookup;
	}
	public List<Command> getCommands() {
		return commands;
	}
	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}
	public List<Command> commands;
}
