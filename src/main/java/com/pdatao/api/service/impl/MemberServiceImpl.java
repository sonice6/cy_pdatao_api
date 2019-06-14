package com.pdatao.api.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.CashBackMapper;
import com.pdatao.api.dao.MemberMapper;
import com.pdatao.api.entity.CashBack;
import com.pdatao.api.entity.CodeData;
import com.pdatao.api.entity.Member;
import com.pdatao.api.service.MemberService;
import com.pdatao.api.util.AppMD5Utils;

@Component
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberMapper memberDao;
	
	@Autowired
	private CashBackMapper cashDao;
	
	@Override
	public Member getMember(String userName, String password) {
		// TODO Auto-generated method stub
		password = AppMD5Utils.getMD5(password);
		return memberDao.getMember(userName, password);
	}

	@Override
	public int register(Member member) {
		// TODO Auto-generated method stub
		member.setPassword(AppMD5Utils.getMD5(member.getPassword()));
		if(member.getWxid()==null)
		{
			member.setWxid("");
		}
		if(member.getWwAccount()==null||memberDao.isExistsWw(member.getWwAccount())==null)
		{
		member.setStatus(2);	
		}else
		{
			member.setStatus(0);
		}
		return memberDao.register(member);
	}

	@Override
	public Member getMemberByuserName(String userName) {
		// TODO Auto-generated method stub
		return memberDao.getMemberByuserName(userName);
	}

	@Override
	public Member getMemberById(Long id) {
		// TODO Auto-generated method stub
		return memberDao.getMemberById(id);
	}

	@Override
	public int updateMemberOrderBs(Long id, String orderBs) {
		// TODO Auto-generated method stub
		return memberDao.updateMemberOrderBs(id, orderBs);
	}

	@Override
	public int updateMemberTrueNum(Long userId) {
		// TODO Auto-generated method stub
		return memberDao.updateMemberTrueNum(userId);
	}

	@Override
	public List<CodeData> getCodeData() {
		// TODO Auto-generated method stub
		return memberDao.getCodeData();
	}

	@Override
	public int updateCodeData(CodeData codeData) {
		// TODO Auto-generated method stub
		return memberDao.updateCodeData(codeData);
	}

	@Override
	public CodeData isExistsWw(String wwAccount) {
		// TODO Auto-generated method stub
		return memberDao.isExistsWw(wwAccount);
	}

	@Override
	public int updateMemberImage(Long id, String path) {
		// TODO Auto-generated method stub
		return memberDao.updateMemberImage(id, path);
	}

	@Override
	public Member getMemberBynickName(String nickName,String nickNames) {
		// TODO Auto-generated method stub
		return memberDao.getMemberBynickName(nickName,nickNames);
	}

	@Override
	public int realName(Member member) {
		// TODO Auto-generated method stub
		return memberDao.realName(member);
	}

	@Override
	public int updateMemberVip(Member member) {
		// TODO Auto-generated method stub
		return memberDao.updateMemberVip(member);
	}

	@Override
	public Member getCash(Long userId, BigDecimal cashAmount) {
		// TODO Auto-generated method stub
		 //插入红包信息
		CashBack cashBack = new CashBack();
		cashBack.setCashbackCode(System.currentTimeMillis()+"");
		cashBack.setCashbackOrder("000000");
		cashBack.setCashbackBuyer(userId.toString());
		cashBack.setCashbackAmount(new BigDecimal("-"+cashAmount.toString()));
		cashBack.setCashbackType(1);
		cashBack.setCashbackState(0);
		cashDao.CashBackAdd(cashBack);
		return memberDao.getMemberById(userId);
	}

	@Override
	public Member getMemberByPhone(String phoneNo) {
		// TODO Auto-generated method stub
		return memberDao.getMemberByPhone(phoneNo);
	}

	@Override
	public Integer getIpCount(String ip) {
		// TODO Auto-generated method stub
		return memberDao.getIpCount(ip);
	}

	@Override
	public int WeRegister(Member member) {
		// TODO Auto-generated method stub
		return memberDao.WeRegister(member);
	}

	@Override
	public int updatePassword(Long id, String password) {
		// TODO Auto-generated method stub
		return memberDao.updatePassword(id, password);
	}

	@Override
	public int updatePhoneNo(Long id, String phoneNo) {
		// TODO Auto-generated method stub
		return memberDao.updatePhoneNo(id, phoneNo);
	}

	@Override
	public BigDecimal getCashAmount(Long id) {
		// TODO Auto-generated method stub
		return memberDao.getCashAmount(id);
	}

	@Override
	public int updateAccount(Member member) {
		// TODO Auto-generated method stub
		return memberDao.updateAccount(member);
	}

	@Override
	public int deleteMember(Long id) {
		// TODO Auto-generated method stub
		return memberDao.deleteMember(id);
	}

}
