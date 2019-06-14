package com.pdatao.api.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_code_data")
@Entity
public class CodeData {
	
	private Long id;
	@Column(name = "wechat_code")
	private String wechatCode;    //微信账号
	@Column(name = "taobao_code")
	private String taobaoCode;   //淘宝账号
	@Column(name = "status")
	private String status;    //状态
	@Column(name = "remark")
	private String remark;   //备注
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
	@Column(name = "sex")
	private int sex;
	@Column(name = "promoted_type")
	private int promotedType;
}
