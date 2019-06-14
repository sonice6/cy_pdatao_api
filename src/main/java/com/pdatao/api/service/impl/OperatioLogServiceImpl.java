package com.pdatao.api.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.pdatao.api.dao.MemberMapper;
import com.pdatao.api.dao.OperationLogMapper;
import com.pdatao.api.entity.CodeData;
import com.pdatao.api.entity.Member;
import com.pdatao.api.entity.OperationLog;
import com.pdatao.api.service.OperationLogService;
import com.pdatao.api.service.WeChatService;
import com.pdatao.api.util.AppMD5Utils;

@Component
public class OperatioLogServiceImpl implements OperationLogService {
	

	@Autowired
	private OperationLogMapper operationDao;
	
	
	public void saveOperationLog(OperationLog operationLog) {
		operationDao.saveOperationLog(operationLog);
		
	}
	
	public void deleteOperationLog(String ids) {
		if(StringUtils.isNotBlank(ids)){
			operationDao.deleteOperationLog("deleteOperationLogDao", ids.split(","));
	        }
		
	}
	
	
}
