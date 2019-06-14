package com.pdatao.api.service;


import com.pdatao.api.entity.UserCenter;

public interface UserService {
	
	
	int addUser(UserCenter userCenter);
	
    UserCenter  selectByUsername(String userName);

}
