package com.kishore.sb;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.kishore.sb.model.CommandInfo;
import com.kishore.sb.model.CommandStatus;

@Component
public class GlobalData {

	private Set<CommandInfo> info = new HashSet<>();

	public void setCommandInfo(Integer commandId, CommandStatus status, String comment) {
		CommandInfo commandInfo = new CommandInfo(commandId, status, comment);
		this.info.remove(commandInfo);
		this.info.add(commandInfo);
	}

	public CommandInfo getCommandInfo(Integer commandId) {
		Optional<CommandInfo> found = info.stream().filter(i -> i.getId().equals(commandId)).findFirst();
		if(found.isPresent()) {
			return found.get();
		} else {
			return new CommandInfo(commandId, CommandStatus.CREATED, "Not run");
		}
	}

}
