package com.pdatao.api.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.pdatao.api.dao.MemberMapper;
import com.pdatao.api.dao.TbUserMapper;
import com.pdatao.api.entity.CodeData;
import com.pdatao.api.entity.Member;
import com.pdatao.api.entity.TbUser;
import com.pdatao.api.service.TbUserService;
import com.pdatao.api.service.WeChatService;
import com.pdatao.api.util.AppMD5Utils;

@Component
public class TbUserServiceImpl implements TbUserService {
	

	@Autowired
	private TbUserMapper tbUserDao;

	@Override
	public TbUser selectByUsername(String username) {
		// TODO Auto-generated method stub
		return tbUserDao.selectByUsername(username);
	}

	@Override
	public int updateByPrimaryKeySelective(TbUser user) {
		// TODO Auto-generated method stub
		return tbUserDao.updateByPrimaryKeySelective(user);
	}
	
}
