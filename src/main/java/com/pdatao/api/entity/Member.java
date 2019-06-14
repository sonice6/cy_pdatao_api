package com.pdatao.api.entity;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_member")
@Entity
public class Member {
	
	private Long id;
	@Column(name = "username")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "bank_account")
	private String BankAccount;
	@Column(name = "bank_type")
	private String BankType;
	@Column(name = "bank_name")
	private String BankName;
	@Column(name = "ww_account")
	private String wwAccount;
	@Column(name = "wx_account")
	private String wxAccount;
	@Column(name = "phone_no")
	private String phoneNo;
	@Column(name = "email")
	private String email;
	@Column(name = "status")
	private int status;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "update_date")
	private Date updateDate;
	@Column(name = "tb_created")
	private Date tbCreated;
	@Column(name = "total_num")
	private int totalNum;
	@Column(name = "good_num")
	private int goodNum;
	@Column(name = "vip_info")
	private String vipInfo;
	@Column(name = "warning_num")
	private int warningNum;
	@Column(name = "wxid")
	private String wxid;
	@Column(name = "wx_nickname")
	private String wxNickname;
	@Column(name = "wx_image")
	private String  wxImage;
	@Column(name = "order_bs")
	private String orderBs;
	@Column(name = "true_num")
	private Integer trueNum;
	@Column(name = "ww_bindnum")
	private Integer wwBindnum;
	@Column(name = "taobao_image")
	private String taobaoImage;
	@Column(name = "wx_unionid")
	private String wxUnionid;
	@Column(name = "tb_name")
	private String tbName;
	@Column(name = "tb_card")
	private String tbCard;
	@Column(name = "tb_vip")
	private Integer tbVip;
	@Column(name = "tb_vip_level")
	private Integer tbVipLevel;
	@Column(name = "tb_sex")
	private Integer tbSex;
	@Column(name = "tb_promoted")
	private Integer tbPromoted;
	@Column(name = "remark")
	private String remark;
	private BigDecimal cashbackAmount;
	@Column(name = "ip")
	private String ip;
}
