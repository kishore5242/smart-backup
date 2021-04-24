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
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private String extensions;
	
	@Column
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
