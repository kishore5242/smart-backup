package com.kishore.sb.model;

import java.util.Set;

public class Operation {

	private Integer id;
	private String name;
	private Job job;
	private Set<Category> categories;
	private boolean avoidDuplication;

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

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public boolean getAvoidDuplication() {
		return avoidDuplication;
	}

	public void setAvoidDuplication(boolean avoidDuplication) {
		this.avoidDuplication = avoidDuplication;
	}

}
