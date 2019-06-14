package com.pdatao.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_cashback")
@Entity
public class CashBack {
	
	private Long id;
	@Column(name = "cashback_code")
	
	private String cashbackCode;
	@Column(name = "cashback_order")
	
	private String cashbackOrder;
	@Column(name = "cashback_amount")
	
	private BigDecimal cashbackAmount;
	@Column(name = "cashback_buyer")
	
	private String cashbackBuyer;
	
	@Column(name = "cashback_time")
	private Date cashbackTime;

	@Column(name = "cashback_type")
	private Integer cashbackType;
	
	@Column(name = "cashback_state")
	private Integer cashbackState;
	}


