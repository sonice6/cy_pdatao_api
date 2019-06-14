package com.pdatao.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.TbGoodsDbMapper;
import com.pdatao.api.entity.TbGoodsDb;
import com.pdatao.api.service.TbGoodsDbService;

@Component
public class TbGoodsDbServiceImpl implements TbGoodsDbService {
	
	
	@Autowired
	private TbGoodsDbMapper goodsDbDao;

	@Override
	public int saveGoodsDb(TbGoodsDb goodsDb) {
		// TODO Auto-generated method stub
		return goodsDbDao.saveGoodsDb(goodsDb);
	}

}
