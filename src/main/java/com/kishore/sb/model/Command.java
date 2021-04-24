package com.kishore.sb.model;

import java.io.File;

public class Command {

	private final File lhs;
	private final String operation;
	private final File rhs;
	private final String comment;

	public Command(File lhs, String operation, File rhs, String comment) {
		this.lhs = lhs;
		this.operation = operation;
		this.rhs = rhs;
		this.comment = comment;
	}

	public File getLhs() {
		return lhs;
	}

	public String getOperation() {
		return operation;
	}

	public File getRhs() {
		return rhs;
	}

	public String getComment() {
		return comment;
	}

}
