package com.kishore.sb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "command")
public class CommandEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	private String lhs;

	@ManyToOne(optional = false)
	@JoinColumn(name = "operation_id", nullable = false)
	private OperationEntity operationEntity;

	@Column
	private String rhs;

	@Column
	private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLhs() {
		return lhs;
	}

	public void setLhs(String lhs) {
		this.lhs = lhs;
	}

	public OperationEntity getOperationEntity() {
		return operationEntity;
	}

	public void setOperationEntity(OperationEntity operationEntity) {
		this.operationEntity = operationEntity;
	}

	public String getRhs() {
		return rhs;
	}

	public void setRhs(String rhs) {
		this.rhs = rhs;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
