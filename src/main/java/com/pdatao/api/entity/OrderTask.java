package com.pdatao.api.entity;

import java.util.Date;

public class OrderTask {
	
	private String address;
	private String receiverName;
	private String logistics;
	private String status;
	private String payment;
	private boolean error = true;
	private String message;
	private Date created;
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getLogistics() {
		return logistics;
	}
	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	@Override
	public String toString() {
		return "OrderTask [address=" + address + ", receiverName="
				+ receiverName + ", logistics=" + logistics + ", status="
				+ status + ", payment=" + payment + ", error=" + error
				+ ", message=" + message + ", created=" + created + "]";
	}
	
}
