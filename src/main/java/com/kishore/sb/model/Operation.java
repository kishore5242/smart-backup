package com.kishore.sb.model;

public class Operation {

	private Integer id;
	private String name;
	private String extensions;
	private boolean organize;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtensions() {
		return extensions;
	}

	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}

	public boolean isOrganize() {
		return organize;
	}

	public void setOrganize(boolean organize) {
		this.organize = organize;
	}

}
