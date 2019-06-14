package com.pdatao.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_order")
@Entity
public class Order {
	
	private Long id;
	@Column(name = "order_no")
	private String orderNo;    //订单编号
	@Column(name = "status")
	private Integer status;   //订单状态 0：待付款,1：待审核,2：审核通过,3：审核不通过,4：已返款,5：已过期,10：订单异常
	private String statusText;
	@Column(name = "buyer")
	private Long buyer;    //买家id
	private String buyerCode;//买家账号
	
	@Column(name = "seller")
	private Long seller;   //卖家id
	private String sellerCode;//卖家账号
	
	@Column(name = "shop_id")
	private Long shopId;    //店铺id
	@Column(name = "shop_name")
	private String shopName;    //店铺名称
	@Column(name = "taobao_order")
	private String taobaoOrder;         //淘宝订单号
	@Column(name = "goods_id")
	private String goodsId;   //商品id
	@Column(name = "gift_id")
	private String giftId;        //礼品ID，多个礼品用逗号隔开
	@Column(name = "address_id")
	private Long addressId;//收获地址id
	@Column(name = "over_date")
	private Date overDate;     //超期时间
	@Column(name = "create_user")
	private String createUser;    //创建人
	@Column(name = "create_date")
	private Date createDate;      //创建时间
	@Column(name = "update_date")
	private Date updateDate;      //创建时间
	@Column(name = "taobao_order_status")
	private Integer taobaoOrderStatus;       //淘宝订单的状态 0:已发货,1：已付款,2：已签收,3：未发货
	@Column(name = "taobao_address")
	private String taobaoAddress;         //买家淘宝收货地址
	@Column(name = "taobao_amount")
	private BigDecimal taobaoAmount;          //淘宝实际交易金额
	@Column(name = "byer_amount")
	private BigDecimal byerAmount;    //买家自己输入交易金额
	@Column(name = "i_refunds")
	private Integer iRefunds; //是否返款
	@Column(name = "status_details")
	private String statusDetails;    //状态明细，审核不通过的原因
	private int goodsEval;//是否需要评价  0 不需要  1需要
	private String isEval;//是否评价过
	private int refundsType;//返款类型0付款返款    1确认收货返款
	private Goods goods;//商品信息
	private Long evalId;//评价id
	@Column(name = "keywords")
	private String keywords;
	
	public Long getEvalId() {
        return evalId;
    }

    public void setevalId(Long evalId) {
        this.evalId= evalId;
    }
    
    
	public int getRefundsType() {
        return refundsType;
    }

    public void setRefundsType(int refundsType) {
        this.refundsType = refundsType;
    }
	

	public int getGoodsEval() {
        return goodsEval;
    }

    public void setGoodsEval(int goodsEval) {
        this.goodsEval = goodsEval;
    }

	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBuyer() {
        return buyer;
    }

    public void setBuyer(Long buyer) {
        this.buyer = buyer;
    }

    public Long getSeller() {
        return seller;
    }

    public void setSeller(Long seller) {
        this.seller = seller;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getTaobaoOrder() {
        return taobaoOrder;
    }

    public void setTaobaoOrder(String taobaoOrder) {
        this.taobaoOrder = taobaoOrder;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getTaobaoOrderStatus() {
        return taobaoOrderStatus;
    }

    public void setTaobaoOrderStatus(Integer taobaoOrderStatus) {
        this.taobaoOrderStatus = taobaoOrderStatus;
    }

    public String getTaobaoAddress() {
        return taobaoAddress;
    }

    public void setTaobaoAddress(String taobaoAddress) {
        this.taobaoAddress = taobaoAddress;
    }

    public BigDecimal getTaobaoAmount() {
        return taobaoAmount;
    }

    public void setTaobaoAmount(BigDecimal taobaoAmount) {
        this.taobaoAmount = taobaoAmount;
    }

    public BigDecimal getByerAmount() {
        return byerAmount;
    }

    public void setByerAmount(BigDecimal byerAmount) {
        this.byerAmount = byerAmount;
    }

    public Integer getiRefunds() {
        return iRefunds;
    }

    public void setiRefunds(Integer iRefunds) {
        this.iRefunds = iRefunds;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }


}
