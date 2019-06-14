package com.pdatao.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.GoodsMapper;
import com.pdatao.api.entity.Goods;
import com.pdatao.api.service.GoodsService;

@Component
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsMapper goodsDao;

	@Override
	public List<Goods> getGoodsList(Integer pageSize, Integer count) {
		// TODO Auto-generated method stub
		List<Goods> list = goodsDao.getGoodsList(pageSize, count);
		return list;
	}

	@Override
	public List<Goods> getListByIds(List<Long> ids) {
		// TODO Auto-generated method stub
		return goodsDao.getListByIds(ids);
	}

	@Override
	public Goods getGoodsBYId(Long id) {
		// TODO Auto-generated method stub
		return goodsDao.getGoodsBYId(id);
	}

	@Override
	public List<Goods> getListLikeId(Long id,List<Long> shopId) {
		// TODO Auto-generated method stub
		return goodsDao.getListLikeId(id,shopId);
	}

	@Override
	public int updateDjGoods(Long id) {
		// TODO Auto-generated method stub
		return goodsDao.updateDjGoods(id);
	}

	@Override
	public int getGoodsCount(Long giftId) {
		// TODO Auto-generated method stub
		return goodsDao.getGoodsCount(giftId);
	}

	@Override
	public int getKeywords(String name,Long giftId) {
		// TODO Auto-generated method stub
		return goodsDao.getKeywords(name,giftId);
	}

	@Override
	public int updateNotFond(Long goodsId) {
		// TODO Auto-generated method stub
		return goodsDao.updateNotFond(goodsId);
	}

	@Override
	public int updateKeyword(Long goodsId, String keyword) {
		// TODO Auto-generated method stub
		return goodsDao.updateKeyword(goodsId, keyword);
	}

}
