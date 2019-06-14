package com.pdatao.api.dao;


import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.UserCenter;

import tk.mybatis.mapper.common.Mapper;

public interface UserCenterMapper extends Mapper<UserCenter>{
	
	int addUser(UserCenter userCenter);
	
	UserCenter selectByUsername(@Param("userName")String userName);
	
	public UserCenter getUserBYId(@Param("id")Long id);
	
	
	public int updateJinb(@Param("id")Long id,@Param("coinsCount")Double coinsCount);
}
