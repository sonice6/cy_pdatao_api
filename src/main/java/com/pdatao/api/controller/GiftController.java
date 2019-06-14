package com.pdatao.api.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdatao.api.constants.StatusConstants;
import com.pdatao.api.entity.Gift;
import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.GoodsDetail;
import com.pdatao.api.entity.Order;
import com.pdatao.api.service.GiftService;
import com.pdatao.api.service.GoodsDetailService;
import com.pdatao.api.service.GoodsService;
import com.pdatao.api.service.OrderService;
import com.pdatao.api.util.RsobjectResult;

@RestController
public class GiftController {
	
	@Autowired
	private GiftService giftService;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private GoodsDetailService goodsDetailService;
	
	
	@Autowired
	private OrderService orderService;
	
	
	/***
	 * 获取礼品列表
	 * @param pageSize
	 * @param pageCount
	 * @return
	 */
	@RequestMapping("/getGiftList")
	public RsobjectResult<?> getGiftList(HttpServletResponse response,Integer pageSize,Integer pageCount){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(pageSize==null && pageCount==null){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		List<Gift> list = giftService.getGiftList(pageSize, pageCount);
		int count = giftService.getGiftCount();
		int total = count/pageCount+(count%pageCount>0?1:0);
		return RsobjectResult.defines(StatusConstants.SUSSESS.getCode(), list,String.valueOf(total));

	}
	
	
	/**
	 * 获取礼品详情
	 * @param id
	 * @return
	 *
	 */
	@RequestMapping("/getGift")
	public RsobjectResult<?> getGift(HttpServletResponse response,Long id){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(id==null){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		
		Gift gift = giftService.getGift(id);
		if(gift!=null){
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), gift);
		}
		return RsobjectResult.define(StatusConstants.GIFT_GET_GIFT_NULL.getCode(), StatusConstants.GIFT_GET_GIFT_NULL.getMsg());
	}
	
	/***
	 * 获取礼品可选商品
	 * @param id
	 * @return
	 */
	@RequestMapping("/getGoodsByGift")
	public RsobjectResult<?> getGoodsByGift(HttpServletResponse response,Long id,Long buyer){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(id==null){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		
		List<Order> orderList = orderService.getOrderByShop(buyer);
		
		List<Goods> list = null;
		if(orderList.size()>0){
			List<Long> shopIds = new ArrayList<Long>();
			for (Order or : orderList) {
				shopIds.add(or.getShopId());
			}
			list = goodsService.getListLikeId(id,shopIds);
		}else{
			list = goodsService.getListLikeId(id,null);
		}
		List<Integer> remove = new ArrayList<Integer>();
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Goods goods = list.get(i);
				if(goods.getReleaseType()==0){
					boolean fl = checkDate(goods.getStartTime(),goods.getEndTime());//是否结束了
					if(goods.getSupplement()==0){
					if(fl){
						GoodsDetail detail = goodsDetailService.getGoodsdetail(goods.getId(), new Date());
						if(detail!=null){
							if(detail.getNum()<=0){
								remove.add(i);
							}
						}
					}else{
						if(new Date().getTime()<goods.getStartTime().getTime()){
							remove.add(i);
						}
					}
				  }else{
					  if(fl){
							GoodsDetail detail = goodsDetailService.getGoodsdetail(goods.getId(), new Date());
							if(detail!=null){
								if(detail.getNum()<=0){
									
									remove.add(i);
								}
							}else{
								remove.add(i);
							}
						}else{
							remove.add(i);
						}
				  }
				}
			}
			//list排序 降序
			Collections.sort(remove, new Comparator<Integer>() {
	            public int compare(Integer o1, Integer o2) {
	                return o2 - o1;
	            }
	        });
			if(remove.size()>0){
				for (int i = 0; i < remove.size(); i++) {
//					if(remove.get(i).intValue()==0){
//						list.remove(remove.get(i).intValue());
//					}else{
//						list.remove(remove.get(i).intValue()-i);
//					}
					list.remove(remove.get(i).intValue());
				}
			}
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), list);
		}
		 return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), null);
	}
	public static void main(String[] args) {
		boolean s = checkDate(new Date(),new Date());
		System.out.println(s);
	}
	public static boolean checkDate(Date start1 ,Date end){
//		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHH");
		Date now = new Date();
//		String d1 = fmt.format(start1);
//		String d2 = fmt.format(end);
//		String 
		if(now.getTime()>= start1.getTime()  && now.getTime()< end.getTime() ){
			return true;
		}
		return false;
	}
}
