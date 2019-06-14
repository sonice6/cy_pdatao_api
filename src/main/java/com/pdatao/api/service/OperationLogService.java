package com.pdatao.api.service;

import java.util.List;

import com.pdatao.api.entity.Member;
import com.pdatao.api.entity.OperationLog;

public interface OperationLogService {
	/**
	 * 增加业务日志信息
	 */
	void saveOperationLog(OperationLog operationLog);
	
	/**
	 * 删除业务日志信息
	 */
	void deleteOperationLog(String ids);

}
