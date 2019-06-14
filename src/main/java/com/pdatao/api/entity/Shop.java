package com.pdatao.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_shop")
@Entity
public class Shop {
	
	private Long id;//店铺ID
	@Column(name = "shop_user")
	private String shopUser;// '店主',
	@Column(name = "shop_user_id")
	private Long shopUserId;//店主对应ID',
	@Column(name = "shop_name")
	private String shopName;// '店铺名称',
	@Column(name = "create_user")
	private String createUser;//'创建人',
	@Column(name = "create_date")
	private Date createDate;//'创建日期',
	@Column(name = "update_date")
	private Date updateDate;// '更新日期'
	@Column(name = "currency_shop")
	private Long currencyShop;//是否通用
	@Column(name = "tb_account")
	private String tbAccount;//淘宝帐号

}
