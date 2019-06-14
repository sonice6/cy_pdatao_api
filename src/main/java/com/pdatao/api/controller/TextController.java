package com.pdatao.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdatao.api.entity.Goods;
import com.pdatao.api.service.GoodsService;
import com.pdatao.api.util.RedisUtil;
import com.pdatao.api.util.RsobjectResult;




@RestController
public class TextController {
	
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@RequestMapping(value="/setRedis",method=RequestMethod.GET)
	public RsobjectResult<?> setRedis(String num,String value){
		boolean b =  redisUtil.set(num, value,10);
		
		return RsobjectResult.define("200", b);
	}
	
	@RequestMapping(value="/getRedis",method=RequestMethod.GET)
	public RsobjectResult<?> setRedis(String key){
		Object b =  redisUtil.get(key);
		
		return RsobjectResult.define("200", b.toString());
	}
	//
	@RequestMapping(value="/getGoodsList",method=RequestMethod.GET)
	public RsobjectResult<?> getGoodsList(Integer pageSize,Integer count){
		List<Goods> list = goodsService.getGoodsList(pageSize, count);
		
		
		return RsobjectResult.define("200", list);
	}

}
