package com.pdatao.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.RechargeHistory;


public interface RechargeHistoryMapper {
	
	RechargeHistory getRechargetHistoryByGoodsId(@Param("goodsId")Long goodsId);
	
	int updateDjRecharge(@Param("goodsId")Long goodsId,@Param("rechargeAmount")double rechargeAmount);
	
	
	Double getRechargetHistoryBysellerId(@Param("sellerId")Long sellerId);
	
	
	List<RechargeHistory> getRechaByseller(@Param("sellerId")Long sellerId);
	
	int updateDjCleanByGoodsId(@Param("goodsId")Long goodsId);
	
	int updateDjCleanByseller(@Param("sellerId")Long sellerId);

	int saveRecharge(RechargeHistory recharge);

}
