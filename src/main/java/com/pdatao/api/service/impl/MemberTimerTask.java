package com.pdatao.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.MemberMapper;


@Component
public class MemberTimerTask {
	
	
	@Autowired
	private MemberMapper memberDao;
	
	//每天中午12点重置 买手可用任务次数
	@Scheduled(cron= "0 0 1 * * ?")
	public void resetMemberTrueNum(){
		memberDao.resetMemberTrueNum();
	}

}
