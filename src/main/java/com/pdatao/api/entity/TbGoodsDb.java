package com.pdatao.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_goods_db")
@Entity
public class TbGoodsDb {
	
	private Long id;
	@Column(name = "goods_id")
	private String goodsId;// 平台任务id
	@Column(name = "ww_account")
	private String wwAccount;//买手淘宝帐号
	@Column(name = "tb_goods_id")
	private String tbGoodsId;// 淘宝商品id
	@Column(name = "keywords")
	private String keywords;//关键字
	@Column(name = "return_json")
	private String returnJson;//返回json
	@Column(name = "create_date")
	private Date createDate;// 创建日期

}
