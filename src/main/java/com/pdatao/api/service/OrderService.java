package com.pdatao.api.service;

import java.util.List;

import com.pdatao.api.entity.Order;


public interface OrderService {
	
	/**
	 * 查询订单列表
	 * @param order
	 * @return
	 */
	public List<Order> getOrder(Integer userId,String status);
	
	/**
	 * 增加或者修改订单列表
	 * @param order
	 * @return
	 */
	public int saveOrUpdateOrder(Order order);
	
	//修改订单
	public int updateOrderCode(Order order);
	
	public List<Order> getByuerOrder(Long userId);
	
	public List<Order> getOrderByShop(Long byuer);
	
	
	public int updateOrderStatus(Long orderId,int status);
	
	public int getByuerStatus(Long byuer);
	
	public Order getOrderById(Long id);
	
	public Order getOrderByTaobao(String taobaoOrder);
	
	public void updateGoodsStock(long id ,int status );
	public void updateGift(long id ,int status );
	public void updateOrderHistory(long id ,int status );

}
