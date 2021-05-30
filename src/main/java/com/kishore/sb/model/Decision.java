package com.kishore.sb.model;

import java.io.File;

public class Decision {

	private File source;
	private Action action;
	private File destination;
	private Category category;
	private String timeStamp;

	public Decision(File source, String timeStamp) {
		this.source = source;
		this.timeStamp = timeStamp;
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public File getDestination() {
		return destination;
	}

	public void setDestination(File destination) {
		this.destination = destination;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
