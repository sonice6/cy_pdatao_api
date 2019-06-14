package com.pdatao.api.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.CashBack;


public interface CashBackMapper {

    int CashBackAdd(CashBack cashBack);
    

	List<CashBack> getCashList(@Param("id")Long id,@Param("pageSize")Integer pageSize,@Param("pageCount")Integer pageCount);
	
	int getCashCount(@Param("id")Long id);

}
