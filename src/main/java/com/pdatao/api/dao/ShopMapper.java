package com.pdatao.api.dao;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

import com.pdatao.api.entity.Shop;

public interface ShopMapper extends Mapper<Shop> {
	
	Shop getShopById(@Param("shopId")Long shopId);
	
}
