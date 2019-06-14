package com.pdatao.api.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_goods")
@Entity
public class Goods {
	
	private Long id;
	@Column(name = "goods_name")
	private String goodsName;    //商品名称
	@Column(name = "goods_title")
	private String goodsTitle;   //商品标题
	@Column(name = "goods_type")
	private String goodsType;    //商品类型
	@Column(name = "goods_image")
	private String goodsImage;   //商品图片
	@Column(name = "gift_type")
	private Integer giftType;    //礼品类型
	@Column(name = "gift")
	private String gift;         //礼品
	@Column(name = "trial_type")
	private Integer trialType;   //试用类型 0：有礼品 , 1：没有礼品
	@Column(name = "shop")
	private Integer shop;        //店铺
	@Column(name = "goods_price")
	private BigDecimal goodsPrice;//商品单价
	@Column(name = "goods_num")
	private Integer goodsNum;     //派单数量（商品数量）
	@Column(name = "demand_num")
	private Integer demandNum;    //需拍件数（一次购买多少个商品）
	@Column(name = "release_type")
	private int releaseType;      //发布模式 0：平均分配,1：极速发布
	@Column(name = "start_time")
	private Date startTime;       //试用开始时间
	@Column(name = "end_time")
	private Date endTime;         //试用结束时间
	@Column(name = "end_type")
	private int endType;          //结束模式 0：继续发布 , 1：结束任务
	@Column(name = "taobao_link")
	private String taobaoLink;    //淘宝链接
	@Column(name = "taobao_details")
	private String taobaoDetails; //淘宝详情
	@Column(name = "create_user")
	private String createUser;    //创建人
	@Column(name = "create_date")
	private Date createDate;      //创建日期
	@Column(name = "update_date")
	private Date updateDate;      //更新日期
	private int status;           //商品状态 0：上架,1：下架,2：删除
	private String sku;           //商品sku信息（颜色、规则、尺寸）
	@Column(name = "shop_name")
	private String shopName;
	@Column(name = "keywords")
	private String keywords;
	@Column(name = "keyword")
	private String keyword;
	@Column(name = "TB_goods_id")
	private String TBgoodsId;
	@Column(name = "goods_demend")
	private String goodsDemend;  //任务描述
	@Column(name = "is_condi")
	private int isCondi = 0;//是否卡价格 0不卡    1卡
	@Column(name = "condi_area")
    private String condiArea;//条件地区
	@Column(name = "condi_price")
    private String condiPrice;//条件价格
	@Column(name = "supplement")
	private int supplement;
	@Column(name = "notFond") //未找到商品点击数量
	private Long notFond;

}
