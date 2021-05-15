package com.kishore.sb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "operation")
public class OperationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	private String name;

	@Column
	private String job;

	@Column
	private String categories;
	
	@Column
	private boolean avoidDuplication;
	
	@Column
	private boolean deleteFromSource;

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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public boolean getAvoidDuplication() {
		return avoidDuplication;
	}

	public void setAvoidDuplication(boolean avoidDuplication) {
		this.avoidDuplication = avoidDuplication;
	}

	public boolean getDeleteFromSource() {
		return deleteFromSource;
	}

	public void setDeleteFromSource(boolean deleteFromSource) {
		this.deleteFromSource = deleteFromSource;
	}

}
