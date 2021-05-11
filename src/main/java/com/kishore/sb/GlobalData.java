package com.kishore.sb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.kishore.sb.model.CommandInfo;
import com.kishore.sb.model.CommandStatus;

@Component
public class GlobalData {

	private Map<Integer, CommandInfo> info = new ConcurrentHashMap<>();

	public void setCommandInfo(Integer commandId, CommandStatus status, String comment) {
		CommandInfo existing = info.putIfAbsent(commandId, new CommandInfo(commandId, status, comment));
		if(existing != null) {
			existing.setComment(comment);
			existing.setStatus(status);
		}
	}
	
	public void resetCommandInfo(Integer commandId, CommandStatus status, String comment) {
		info.remove(commandId);
		info.put(commandId, new CommandInfo(commandId, status, comment));
	}

	public CommandInfo getCommandInfo(Integer commandId) {
		CommandInfo cmdInfo = info.get(commandId);
		if(cmdInfo == null) {
			cmdInfo = new CommandInfo(commandId, CommandStatus.NOT_RUN, "Not run");
		}
		return cmdInfo;
	}

}
