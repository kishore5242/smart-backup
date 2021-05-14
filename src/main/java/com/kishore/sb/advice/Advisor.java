package com.kishore.sb.advice;

import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;

public abstract class Advisor {
	
	protected Command command;

	protected Advisor(Command command) {
		this.command = command;
	}

	public abstract boolean advise(Decision decision);

}
