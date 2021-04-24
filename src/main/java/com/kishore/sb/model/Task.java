package com.kishore.sb.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Task {

	private String name;
	private final Set<Command> commands = new HashSet<>();

	public Task(String name, Collection<Command> commands) {
		this.name = name;
		this.commands.addAll(commands);
	}

	public String getName() {
		return name;
	}

	public Set<Command> getCommands() {
		return commands;
	}

}
