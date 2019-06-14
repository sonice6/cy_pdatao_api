package com.pdatao.api.service;

import java.util.List;



import com.pdatao.api.entity.Gift;

public interface GiftService {
	
	List<Gift> getGiftList(Integer pageSize,Integer pageCont);
	
	
	Gift getGift(Long id);
	
	
	int updateDjGift(Long id);
	
	int getGiftCount();

}
