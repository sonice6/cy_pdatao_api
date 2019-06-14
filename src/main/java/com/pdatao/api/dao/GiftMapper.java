package com.pdatao.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

import com.pdatao.api.entity.Gift;

public interface GiftMapper extends Mapper<Gift>{
	
	List<Gift> getGiftList(@Param("pageSize")Integer pageSize,@Param("pageCount")Integer pageCount);
	
	Gift getGift(@Param("id")Long id);
	
	
	int updateDjGift(@Param("id")Long id);
	
	int getGiftCount();
}
