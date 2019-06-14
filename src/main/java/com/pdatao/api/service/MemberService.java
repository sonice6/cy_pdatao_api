package com.pdatao.api.service;

import java.math.BigDecimal;
import java.util.List;

import com.pdatao.api.entity.CodeData;
import com.pdatao.api.entity.Member;


public interface MemberService {
	
	BigDecimal getCashAmount(Long id);
	
	Member getMember(String userName,String password);
	
	int register(Member member);
	
	Member getMemberByuserName(String userName);
	
	Member getMemberBynickName(String nickName,String nickNames);
	
	
	Member getMemberById(Long id);
	
	
	int realName(Member member);
	
	int updateMemberVip(Member member);
	
	int updateMemberOrderBs(Long id,String orderBs);
	
	int updateMemberTrueNum(Long userId);
	
	int updateMemberImage(Long id,String path);
	
	List<CodeData> getCodeData();
	
	CodeData isExistsWw(String wwAccount);
	
	int updateCodeData(CodeData codeData);
	
	Member getCash(Long userId,BigDecimal cashAmount);
	
	Member getMemberByPhone(String phoneNo);
	
	Integer getIpCount(String ip);

	
	int WeRegister(Member member);
	
	int updatePassword(Long id,String password);
	
	int updatePhoneNo(Long id,String phoneNo);
	
	int updateAccount(Member member);
	
	int deleteMember(Long id );
}
