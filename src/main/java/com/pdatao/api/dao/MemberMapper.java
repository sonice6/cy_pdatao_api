package com.pdatao.api.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pdatao.api.entity.CodeData;
import com.pdatao.api.entity.Member;


public interface MemberMapper {
	
	BigDecimal getCashAmount(@Param("id")Long id);
	
	Member getMember(@Param("userName")String userName,@Param("password")String password);
	
	Member getMemberByUnionid(@Param("wxUnionid")String wxUnionid);
	
	List<Member> isExistsWwAccount(@Param("wwAccount") String wwAccount);
	
	List<Member> isExistsWxAccount(@Param("wxAccount") String wxAccount);
	
	int register(Member member);
	
	int WeRegister(Member member);
	
	int BindBankCard(@Param("id")Long id ,@Param("bankName")String bankName,@Param("bankType")String bankType,@Param("bankAccount")String bankAccount,@Param("wxAccount")String wxAccount);
	
	Member getMemberByuserName(@Param("userName")String userName);
	
	Member getMemberBynickName(@Param("nickName")String nickName,@Param("nickNames")String nickNames);
	
	Member getMemberById(@Param("id")Long id);
	
	Member getMemberByOpenId(@Param("openid")String openid);
	
	int realName(Member member);
	
	int updateMemberVip(Member member);
	
	int updateMemberOrderBs(@Param("id")Long id,@Param("orderBs")String orderBs);
	
	int updateMemberTrueNum(@Param("userId")Long userId);
	
	int updateMemberImage(@Param("id")Long id ,@Param("path")String path);
	
	int updateMemberUnionid(@Param("userId")Long userId,@Param("wxUnionid")String wxUnionid);
	
	CodeData isExistsWw(@Param("wwAccount")String wwAccount);
	
	int updateMemberWwAccount(Member member);
	
	int resetMemberTrueNum();
	
	List<CodeData> getCodeData();
	
	int updateCodeData(CodeData codeData);
	
	Member getMemberByPhone(@Param("phoneNo")String phoneNo);
	
	
	Integer getIpCount(@Param("ip")String ip);
	
	int updatePassword(@Param("id")Long id,@Param("password")String password);
	
	int updatePhoneNo(@Param("id")Long id,@Param("phoneNo")String phoneNo);
	
	int updateAccount(Member member);
	
	int deleteMember(@Param("id")Long id);

}
