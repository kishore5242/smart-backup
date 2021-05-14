package com.kishore.sb.model;

public class CommandInfo {

	private Integer id;
	private CommandStatus status;
	private String comment;
	private StringBuilder console;
	private Integer progress;

	public CommandInfo(Integer id, CommandStatus status, String comment, Integer progress) {
		this.id = id;
		this.status = status;
		this.comment = comment;
		this.console = new StringBuilder(comment);
		this.progress = progress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CommandStatus getStatus() {
		return status;
	}

	public void setStatus(CommandStatus status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
		this.console.append(System.lineSeparator()).append(comment);
	}
	
	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public StringBuilder getConsole() {
		return console;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandInfo other = (CommandInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
