package com.pdatao.api.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.CashBack;

public interface CashBackService {
	
	int CashBackAdd(CashBack cashBack);
	
	List<CashBack> getCashList(Long id,Integer pageSize, Integer pageCont);
	
	int getCashCount(Long id);

}
