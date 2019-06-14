package com.pdatao.api.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.GoodsMapper;
import com.pdatao.api.dao.MemberMapper;
import com.pdatao.api.dao.OrderMapper;
import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.GoodsDetail;
import com.pdatao.api.entity.Member;
import com.pdatao.api.entity.Order;
import com.pdatao.api.service.OrderService;

@Component
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderDao;
	
	@Autowired
	private MemberMapper memberDao;
	
	@Autowired
	private GoodsMapper goodsDao;

	@Override
	public List<Order> getOrder(Integer userId, String status) {
		// TODO Auto-generated method stub
		List<Order> list;
		if (status != null) {
			list = orderDao.getOrderList(userId, 1);
		} else {
			list = orderDao.getOrderList(userId, 0);
		}

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String id = list.get(i).getGoodsId();
				Goods goods = orderDao.getGoodsList(id);
				list.get(i).setGoods(goods);
			}
		}
		return list;
	}
	
	@Override
	public int updateOrderCode(Order order) {
		if (order != null) {
			List<Order> orders = orderDao.getOrder(order.getId(),null);
			String code = order.getTaobaoOrder().replace(",", "");
			System.out.println("********************");
			System.out.println(code.substring(code.length()-6));
			System.out.println(orders.get(0).getBuyer());
			if(orderDao.getOrder(orders.get(0).getBuyer(),"%"+code.substring(code.length()-6)).size()>0)
			{
				return -3;
			}
			Member member = memberDao.getMemberById(orders.get(0).getBuyer());
			if(!member.getOrderBs().equals("0"))
			{
				if(member.getOrderBs().indexOf(code.substring(code.length()-6))==-1)
				{
				return -3;
				}
			}
			//验证订单号是否重复
			orders = orderDao.getOrderCode(order.getId(),code); 
			if(orders.size()>0)
			{
				return -2;
			}
			return orderDao.updateOrderCode(order);
		}
		return 0;
	}

	public int saveOrUpdateOrder(Order order) {
		if (order != null) {
			if (order.getId() != null) {// 修改a
				if(order.getTaobaoOrder()!=null)
				{
					List<Order> orders = orderDao.getOrder(order.getId(),null);
					String code = order.getTaobaoOrder().replace(",", "");
					System.out.println("********************");
					System.out.println(code.substring(code.length()-4));
					System.out.println(orders.get(0).getBuyer());
					if(orderDao.getOrder(orders.get(0).getBuyer(),code.substring(code.length()-4)+"?").size()>0)
					{
						return -1;
					}
					Member member = memberDao.getMemberById(orders.get(0).getBuyer());
					if(!member.getOrderBs().equals("0"))
					{
						if(member.getOrderBs().indexOf(code.substring(code.length()-4))==-1)
						{
						return -1;
						}
					}
					orders = orderDao.getOrder(null,code); 
					if(orders.size()>0)
					{
						return -2;
					}
					
				}
				
				return orderDao.updateOrder(order);
			} else {// 添加

				Goods goods = goodsDao.getGoodsBYId(Long.parseLong(order.getGoodsId()));
				if(goods==null)
				{
				return 26;	
				}
				if(goods.getGoodsNum()<=0){
					return 23;
				}
				if(goods.getReleaseType()==0){
					GoodsDetail detail = goodsDao.getGoodsdetail(goods.getId(), new Date());
					if(detail!=null){
						if(detail.getNum()<=0){
							return 23;
						}
					}
				}
				Member member = memberDao.getMemberById(order.getBuyer());
				
				if(member.getTrueNum()<=0){
					return 24;
				}
				List<Order> list = orderDao.getByuerOrder(order.getBuyer());
				
				if(list.size()>0){
					return 25;
				}
				
				long l = 0;
				if (order.getTaobaoOrder() == null) {
					order.setTaobaoOrder("");
				}
				if (order.getGiftId() == null) {
					order.setGiftId("");
				}
				if (order.getAddressId() == null) {
					order.setAddressId(l);
				}
				if (order.getTaobaoOrderStatus() == null) {
					order.setTaobaoOrderStatus(0);
				}
				if (order.getTaobaoAmount() == null) {
					order.setTaobaoAmount(new BigDecimal("0"));
				}
				if (order.getByerAmount() == null) {
					order.setByerAmount(new BigDecimal("0"));
				}
				if (order.getStatusDetails() == null) {
					order.setStatusDetails("");
				}
				if (order.getSeller() == null) {
					order.setSeller(orderDao.getShopUserId(order.getShopId()));
				}
				if (order.getStatus() == null) {
					order.setStatus(0);
				}
				memberDao.updateMemberTrueNum(order.getBuyer());//扣除买手可用次数
				return orderDao.saveOrder(order);
			}
		}
		return  0;
	}

	@Override
	public List<Order> getByuerOrder(Long userId) {
		// TODO Auto-generated method stub
		return orderDao.getByuerOrder(userId);
	}

	@Override
	public List<Order> getOrderByShop(Long byuer) {
		// TODO Auto-generated method stub
		return orderDao.getOrderByShop(byuer);
	}

	@Override
	public int updateOrderStatus(Long orderId, int status) {
		// TODO Auto-generated method stub
		return orderDao.updateOrderStatus(orderId, status);
	}

	@Override
	public int getByuerStatus(Long byuer) {
		// TODO Auto-generated method stub
		return orderDao.getbyuerStatus(byuer);
	}

	@Override
	public Order getOrderById(Long id) {
		// TODO Auto-generated method stub
		return orderDao.getOrderById(id);
	}

	@Override
	public Order getOrderByTaobao(String taobaoOrder) {
		// TODO Auto-generated method stub
		return orderDao.getOrderByTaobao(taobaoOrder);
	}
	

	public void updateGoodsStock(long id, int status) {
		// TODO Auto-generated method stub
		orderDao.updateGoodsStock(id, status);
	}

	public void updateGift(long id, int status) {
		// TODO Auto-generated method stub
		orderDao.updateGift(id, status);
	}

	public void updateOrderHistory(long id, int status) {
		// TODO Auto-generated method stub
		orderDao.updateOrderHistory(id, status);
	}

	

}
