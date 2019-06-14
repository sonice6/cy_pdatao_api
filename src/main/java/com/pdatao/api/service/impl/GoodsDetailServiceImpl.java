package com.pdatao.api.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.GoodsMapper;
import com.pdatao.api.entity.GoodsDetail;
import com.pdatao.api.service.GoodsDetailService;

@Component
public class GoodsDetailServiceImpl implements GoodsDetailService {

	
	@Autowired
	private GoodsMapper goodsDao;
	
	@Override
	public GoodsDetail getGoodsdetail(Long goodsId, Date startTime) {
		// TODO Auto-generated method stub
		return goodsDao.getGoodsdetail(goodsId, startTime);
	}

	@Override
	public int jiaGoodsDetailNum(Long id, int num) {
		// TODO Auto-generated method stub
		return goodsDao.jiaGoodsDetailNum(id, num);
	}
	
	

}
