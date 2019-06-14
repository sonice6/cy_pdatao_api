package com.pdatao.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_goods_detail")
@Entity
public class GoodsDetail {

	private Long id;
	@Column(name = "goods_id")
	private Long goodsId;
	@Column(name = "start_time")
	private Date startTime;
	@Column(name = "num")
	private long num;
}
