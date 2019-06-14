package com.pdatao.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.CashBackMapper;
import com.pdatao.api.entity.CashBack;
import com.pdatao.api.service.CashBackService;


@Component
public class CashBackServiceImpl implements CashBackService {

	@Autowired
	private CashBackMapper cashBackDao;
	
	@Override
	public int CashBackAdd(CashBack cashBack) {
		// TODO Auto-generated method stub
		return cashBackDao.CashBackAdd(cashBack);
	}

	@Override
	public List<CashBack> getCashList(Long id,Integer pageSize, Integer pageCont) {
		// TODO Auto-generated method stub
		Integer size = (pageSize-1)*pageCont;
		return cashBackDao.getCashList(id,size,pageCont);
	}

	@Override
	public int getCashCount(Long id) {
		// TODO Auto-generated method stub
		return cashBackDao.getCashCount(id);
	}
	
}
