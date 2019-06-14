package com.pdatao.api.service;

import java.util.List;
import com.pdatao.api.entity.Member;

public interface WeChatService {
	
	//获取微信信息
	Member WechatLogin(String Code,String appId,String appPwd);
	//绑定旺旺
	Member BindWw(Member mem,Integer status);
	
	Member BindBankCard(Long id,String bankName,String bankType,String bankAccount,String wxAccount);
	
	List<Member> isExistsWwAccount(String wwAccount);
	
	List<Member> isExistsWxAccount(String wxAccount);
	
	//通过code找用户信息
	Member WechatApp(String naem,String code);

}
