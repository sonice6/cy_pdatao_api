package com.pdatao.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdatao.api.constants.StatusConstants;
import com.pdatao.api.entity.UserCenter;
import com.pdatao.api.service.UserService;
import com.pdatao.api.util.RsobjectResult;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/***
	 * 卖家注册
	 * @param pageSize
	 * @param pageCount
	 * @return
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public RsobjectResult<?> getGiftList(UserCenter userCenter){
		int num=0;
		if(userCenter!=null)
		{
			num = userService.addUser(userCenter);
		}
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), num);
	}
	
	
}
