package com.pdatao.api.service;

import java.util.List;

import com.pdatao.api.entity.Member;
import com.pdatao.api.entity.TbUser;

public interface TbUserService {
	public TbUser selectByUsername(String username);
	
	 public int updateByPrimaryKeySelective(TbUser user);

}
