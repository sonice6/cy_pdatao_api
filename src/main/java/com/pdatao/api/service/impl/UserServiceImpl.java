package com.pdatao.api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.UserCenterMapper;
import com.pdatao.api.entity.UserCenter;
import com.pdatao.api.service.UserService;
import com.pdatao.api.util.AppMD5Utils;

@Component
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserCenterMapper userDao;

	
	public int addUser(UserCenter userCenter) {
		// TODO Auto-generated method stub
		userCenter.setPassword(AppMD5Utils.getMD5(userCenter.getPassword()));
		userCenter.setEmail(userCenter.getUserName());
		long lon=3;
		userCenter.setRoleId(lon);
		userCenter.setSex(0);
		userCenter.setStatus(0);
		return userDao.addUser(userCenter);
	}


	@Override
	public UserCenter selectByUsername(String userName) {
		// TODO Auto-generated method stub
		return userDao.selectByUsername(userName);
	}

}
