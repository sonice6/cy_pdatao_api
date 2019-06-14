package com.pdatao.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_operation_log")
@Entity
public class OperationLog {
	
	private Long id;
	private String modular;//操作模块,
	private String type;//操作类型 insert update delete',
	private String createUser;//操作人',
	private Date createDate;//创建日期
	private Long modularId;//'对应业务ID',
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getModular() {
		return modular;
	}
	public void setModular(String modular) {
		this.modular = modular;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getModularId() {
		return modularId;
	}
	public void setModularId(Long modularId) {
		this.modularId = modularId;
	}
	
}
