package com.pdatao.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_eval")
@Entity
public class Eval {
	
	private Long id;
	@Column(name = "order_id")
	private Long orderId;    //订单编号
	@Column(name = "buyer")
	private Long buyer;
	@Column(name = "upload_img")
	private String uploadImg;
	@Column(name = "message")
	private String message;  
	

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

	public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    

	public Long getBuyer() {
        return buyer;
    }

    public void setBuyer(Long buyer) {
        this.buyer = buyer;
    }
    

	public String getUploadImg() {
        return uploadImg;
    }

    public void setUploadImg(String uploadImg) {
        this.uploadImg = uploadImg;
    }
    

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
