package com.pdatao.api.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.GoodsDetail;

public interface GoodsMapper extends Mapper<Goods>  {
	
	
	public List<Goods> getGoodsList(@Param("pageSize")Integer pageSize,@Param("count")Integer count);
	
	public List<Goods> getListByIds(@Param("ids")List<Long> ids);
	
	
	public Goods  getGoodsBYId(@Param("id")Long id);
	
	
	public List<Goods> getListLikeId(@Param("id")Long id,@Param("shopId")List<Long> shopId);
	
	
	public int updateDjGoods(@Param("id")Long id);
	
	public int updatePJGoods(@Param("id")Long id);
	
	public int getGoodsCount(@Param("giftId")Long giftId);
	
	public int getKeywords(@Param("name")String name,@Param("giftId")Long giftId);
	
	
	
	public int updateStatusByUserId(@Param("seller")String userName,@Param("status")int status);
	
	public int updatePrice(@Param("id")Long id,@Param("price")double price);
	
	
	GoodsDetail getGoodsdetail(@Param("goodsId")Long goodsId, @Param("startTime")Date startTime);
	
	
	int jiaGoodsDetailNum(@Param("id")Long id,@Param("num")int num);
	
	int updateNotFond(@Param("goodsId")Long goodsId);

	int updateKeyword(@Param("goodsId")Long goodsId,@Param("keyword")String keyword);

}
