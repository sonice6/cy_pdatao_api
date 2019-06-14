package com.pdatao.api.service;

import java.util.List;


import com.pdatao.api.entity.Goods;

public interface GoodsService {
	
	
	public List<Goods> getGoodsList(Integer pageSize,Integer count);
	
	public List<Goods> getListByIds(List<Long> ids);
	
	
	public Goods  getGoodsBYId(Long id);
	
	public List<Goods> getListLikeId(Long id,List<Long> shopId);
	
	public int updateDjGoods(Long id);
	
	public int getGoodsCount(Long giftId);
	
	public int getKeywords(String name,Long giftId);
	
	public int updateNotFond(Long goodsId);
	
	public int updateKeyword(Long goodsId,String keyword);

}
