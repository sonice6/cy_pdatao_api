package com.pdatao.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.GiftMapper;
import com.pdatao.api.entity.Gift;
import com.pdatao.api.service.GiftService;

@Component
public class GiftServiceImpl implements GiftService {
	
	@Autowired
	private GiftMapper giftDao;

	@Override
	public List<Gift> getGiftList(Integer pageSize, Integer pageCont) {
		// TODO Auto-generated method stub
		Integer size = (pageSize-1)*pageCont;

		return giftDao.getGiftList(size, pageCont);
	}

	@Override
	public Gift getGift(Long id) {
		// TODO Auto-generated method stub
		return giftDao.getGift(id);
	}

	@Override
	public int updateDjGift(Long id) {
		// TODO Auto-generated method stub
		return giftDao.updateDjGift(id);
	}

	@Override
	public int getGiftCount() {
		// TODO Auto-generated method stub
		return giftDao.getGiftCount();
	}

}
