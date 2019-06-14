package com.pdatao.api.service;

import java.util.Date;

import com.pdatao.api.entity.GoodsDetail;

public interface GoodsDetailService {
   
	public GoodsDetail getGoodsdetail(Long goodsId,Date startTime);
	
	public int jiaGoodsDetailNum(Long id,int num);
}
