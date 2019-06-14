package com.pdatao.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.Order;


public interface OrderMapper {
	
	/**
     * 查询订单信息
     * @param order
     * @return
     */
	public List<Order> getOrderList(@Param("userId")Integer userId,@Param("status")Integer status);
	
	
	/**
     * 查询订单对象
     * @param order
     * @return
     */
	public List<Order> getOrder(@Param("Id")Long Id,@Param("OrderCode")String OrderCode);
	
	/**
     * 查询订单下的商品信息
     * @param order
     * @return
     */
	public Goods getGoodsList(@Param("id")String Id);
	
	/**
     * 查询订单下的店主id
     * @param order
     * @return
     */
	public Long getShopUserId(@Param("id")Long Id);
	
	/**
     * 增加订单信息
     * @param order
     * @return
     */
    public int saveOrder(Order order);

    /**
     * 修改订单信息
     * @param order
     */
    public int updateOrder(Order order);
    
    //修改订单号
    public int updateOrderCode(Order order);
    
    
    public List<Order> getByuerOrder(@Param("userId")Long userId);
    
    public List<Order> getOrderByShop(@Param("userId")Long byuer);
    
    public List<Order> getOrderTask();
    
    public List<Order> getOrderEval();
    
    public int getbyuerStatus(@Param("byuer")Long byuer);
    
    public int updateTaskOrderStatus(@Param("orderId")Long orderId);//订单自动验证变更手动验证
    
    public int updateOrderAddress(@Param("orderId")Long orderId,@Param("address")String address);
    
    public int updateOrderStatus(@Param("orderId")Long orderId,@Param("status")Integer status);
    
    public int updateOrderStatusDetails(@Param("orderId")Long orderId,@Param("statusDetail")String statusDetail);
    
    public Order getOrderById(@Param("id")Long id);
    
    public Order getOrderByTaobao(String taobaoOrder);
    //获取重复订单号得订单
    public List<Order> getOrderCode(@Param("id")Long id,@Param("orderCode")String orderCode);
    
    public int updateOrderTaobaoAmount(@Param("id")Long id,@Param("taobaoAmount")double taobaoAmount);
    /**
     * 订单库存历史
     * @param ids
     */
    void updateOrderHistory(@Param("orderId") Long orderId,@Param("status") int status);
    
    
    /**
     * 订单库存任务
     * @param ids
     */
    void updateGoodsStock(@Param("orderId") Long orderId,@Param("status") int status);
    /**
     * 订单库存礼品
     * @param ids
     */
    void updateGift(@Param("orderId") Long orderId,@Param("status") int status);
}
