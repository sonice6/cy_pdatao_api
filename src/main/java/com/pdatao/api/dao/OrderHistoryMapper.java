package com.pdatao.api.dao;

import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.OrderHistory;

public interface OrderHistoryMapper {
	
	
	int saveOrderHistory(OrderHistory orderHistory);
	
	 

}
