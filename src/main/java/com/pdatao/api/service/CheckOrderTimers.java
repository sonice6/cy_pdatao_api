package com.pdatao.api.service;

import com.pdatao.api.entity.Order;

public interface CheckOrderTimers {
	
	public void checkOrder(Order order);
	
	String skQueryOrder(String userName,String tid);

}
