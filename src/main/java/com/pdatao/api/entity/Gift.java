package com.pdatao.api.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_gift")
@Entity
public class Gift {
	
	private Long id;
	@Column(name = "gift_type")
	private Integer giftType;
	@Column(name = "gift_name")
	private String giftName;
	@Column(name = "gift_code")
	private Integer giftCode;
	@Column(name = "gift_image")
	private String giftImage;
	@Column(name = "status")
	private int status;
	@Column(name = "gift_stock")
	private Integer giftStock;
	@Column(name = "create_user")
	private String createUser;
	@Column(name = "gift_available_nul")
	private Integer giftAvailableNul;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "updateDate")
	private Date updateDate;
	@Column(name = "goods_id")
	private String goodsId;
	@Column(name = "shopId")
	private Long shopId;
	@Column(name = "shop_name")
	private String shopName;
	
	private List<Goods> goodsList;
}
