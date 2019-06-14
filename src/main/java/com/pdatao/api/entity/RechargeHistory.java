package com.pdatao.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_recharge_history")
@Entity
public class RechargeHistory {
	
	private Long id;
	@Column(name = "recharge_id")
	private Long rechargeId;            //充值对象id
	@Column(name = "recharge_amount")
	private Double rechargeAmount;      //充值金额
	@Column(name = "goods_id")
	private String goodsId;             //商品ID 多个逗号隔开
	@Column(name = "gift_id")
	private String giftId;              //礼品ID 多个逗号隔开
	@Column(name = "type")
	private int type;                   //类型 0：消费 1：充值
	@Column(name = "create_user")
	private String createUser;         //创建人
	@Column(name = "create_date")
	private Date createDate;     
	@Column(name = "update_date")
	private Date updateDate;
	
	
}
