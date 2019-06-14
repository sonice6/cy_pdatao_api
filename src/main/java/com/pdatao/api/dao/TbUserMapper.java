package com.pdatao.api.dao;



import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.TbUser;



public interface TbUserMapper extends tk.mybatis.mapper.common.Mapper<TbUser> {
	
		
	public TbUser selectByUsername(@Param("username") String username);
	
}
