package com.pdatao.api.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.GiftMapper;
import com.pdatao.api.dao.GoodsMapper;
import com.pdatao.api.dao.OrderHistoryMapper;
import com.pdatao.api.entity.GoodsDetail;
import com.pdatao.api.entity.OrderHistory;
import com.pdatao.api.service.OrderHistroyService;

@Component
public class OrderHistroyServiceImpl implements OrderHistroyService {

	
	@Autowired
	private OrderHistoryMapper orderHistroyDao;
	
	@Autowired
	private GiftMapper giftDao;
	
	@Autowired
	private GoodsMapper  goodsDao;
	
	
	@Override
	public int saveOrderHistory(OrderHistory orderHistory) {
		// TODO Auto-generated method stub
		int num = 0 ;
		num  = orderHistroyDao.saveOrderHistory(orderHistory);
		if(num>0){
			giftDao.updateDjGift(orderHistory.getGiftId());
			goodsDao.updateDjGoods(orderHistory.getGoodsId());
			GoodsDetail goodsDetail = goodsDao.getGoodsdetail(orderHistory.getGoodsId(),new Date());
			if(goodsDetail!=null){
				goodsDao.updatePJGoods(goodsDetail.getId());
			}
		}
		return num;
	}

}
