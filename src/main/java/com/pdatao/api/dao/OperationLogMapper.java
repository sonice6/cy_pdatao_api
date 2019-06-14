package com.pdatao.api.dao;



import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.Eval;
import com.pdatao.api.entity.OperationLog;


public interface OperationLogMapper {
	
	/**
	 * 增加业务日志列表
	 * @param operationLog
	 * @return
	 */
	public int saveOperationLog(OperationLog operationLog);
	
	/**
	 * 批量删除业务日志列表
	 * @param ids
	 */
	public void deleteOperationLog(String statement, @Param("ids") String[] ids);
	
}
