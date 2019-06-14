package com.pdatao.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_order_history")
@Entity
public class OrderHistory {
	
	private Long id;
	@Column(name = "buyer")
	private Long buyer;        //买家
	@Column(name = "seller")
	private Long seller;       //卖家
	@Column(name = "shop_id")
	private Long shopId;       //店铺
	@Column(name = "goods_id")
	private Long goodsId;     //任务
	@Column(name = "gift_id")
	private Long giftId;      //礼品
	@Column(name = "num")
	private int num;          //数量
	@Column(name = "order_no") 
	private Long orderNo;     //平台订单号
	@Column(name = "type")
	private int type;        //类型 0:冻结，1:任务完成 ，2:任务失败返回
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "update_date")
	private Date updateDate;
	
}
