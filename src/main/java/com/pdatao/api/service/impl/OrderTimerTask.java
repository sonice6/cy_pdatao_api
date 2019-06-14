package com.pdatao.api.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pdatao.api.constants.TbOrderStatus;
import com.pdatao.api.dao.GoodsMapper;
import com.pdatao.api.dao.MemberMapper;
import com.pdatao.api.dao.OrderMapper;
import com.pdatao.api.dao.RechargeHistoryMapper;
import com.pdatao.api.dao.ShopMapper;
import com.pdatao.api.dao.UserCenterMapper;
import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.Order;
import com.pdatao.api.entity.OrderTask;
import com.pdatao.api.entity.RechargeHistory;
import com.pdatao.api.entity.Shop;
import com.pdatao.api.entity.UserCenter;
import com.pdatao.api.util.ApiClient;

@Component
public class OrderTimerTask {
	
	@Autowired
	private OrderMapper orderDao;
	
	@Autowired
	private MemberMapper memberDao;
	
	@Autowired
	private GoodsMapper goodsDao;
	
	@Autowired
	private RechargeHistoryMapper rechargeDao;
	
	@Autowired
	private UserCenterMapper userCenterDao;
	
	@Autowired
	private ShopMapper shopDao;
	
	//自动完成评价订单
//	@Scheduled(cron= "0 0/10 * * * ?")
//	public void checkOrderEval(){
//		System.out.println("开始评价订单验证-------------");
//		List<Order> list = orderDao.getOrderEval();
//		if(list!=null)
//		{
//			long d = 1000 * 60 * 60 * 24;
//			long h = 1000 * 60 * 60;
//			for(Order order : list)	
//			{
//				Date now = new Date();
//
//		        long diff = now.getTime() - order.getUpdateDate().getTime();
//		        
//		        long day = diff/d;
//		        long hours = diff/h;
//		        //当天得订单 或者2个小时以内得订单
//		        if(day<1 || hours<2){
//		        	orderDao.updateOrderStatus(order.getId(), 4);
//		        }
//			}
//		}
//	}
	
	
	@Scheduled(cron= "0 0/10 * * * ?")
	public void checkOrder(){
		System.out.println("开始执行订单验证-------------");
		List<Order> list = orderDao.getOrderTask();
		System.out.println("列表数量"+list.size());
		long d = 1000 * 60 * 60 * 24;
		long h = 1000 * 60 * 60;
		for (Order order : list) {
			System.out.println("+++++"+order.getShopId());
			Shop shop = shopDao.getShopById(order.getShopId());
			OrderTask orderTask = getOrderParamer(shop.getTbAccount(),order.getTaobaoOrder());
			
			if(orderTask==null){//自动验证订单异常 改为手动验证
				orderDao.updateTaskOrderStatus(order.getId());
				continue;
			}
			if(!orderTask.isError()){
				orderDao.updateOrderStatusDetails(order.getId(), orderTask.getMessage());
				continue;
			}
			
			Date now = new Date();

	        long diff = now.getTime() - orderTask.getCreated().getTime();
	        
	        long day = diff/d;
	        long hours = diff/h;
	        //当天得订单 或者2个小时以内得订单
	        if(day<1 || hours<2){
	        
			System.out.println("0.0.0.0"+orderTask.getStatus());
			if(StringUtils.isEmpty(order.getTaobaoAddress())){
				orderDao.updateOrderAddress(order.getId(), orderTask.getAddress()+","+orderTask.getReceiverName()+","+orderTask.getLogistics());
			}
			if(orderTask.getStatus().equals(TbOrderStatus.WAIT_SELLER_SEND_GOODS) || orderTask.getStatus().equals(TbOrderStatus.WAIT_BUYER_CONFIRM_GOODS)){
				System.out.println("----0"+order.getGoodsId());
				Goods goods = goodsDao.getGoodsBYId(Long.parseLong(order.getGoodsId()));
				if(goods==null){
					System.out.println("商品为空");
					System.out.println("订单编号=="+order.getId());
					continue;
				}
				/*创建消费记录*/
				RechargeHistory  recharges = new RechargeHistory();
				recharges.setGiftId(order.getGiftId());
				recharges.setGoodsId(order.getGoodsId());
				recharges.setType(0);
				recharges.setRechargeId(order.getSeller());
				recharges.setCreateUser(goods.getCreateUser());
				System.out.println("=-----------------"+goods.getGoodsPrice().doubleValue());
				System.out.println("....................."+Double.parseDouble(orderTask.getPayment()));
				if(goods.getGoodsPrice().doubleValue()>Double.parseDouble(orderTask.getPayment())){
					recharges.setRechargeAmount(goods.getGoodsPrice().doubleValue()*100);
				}else{
					recharges.setRechargeAmount(Double.parseDouble(orderTask.getPayment())*100);
				}
				rechargeDao.saveRecharge(recharges);
				
				BigDecimal payment = new BigDecimal(orderTask.getPayment());
				Double amount = rechargeDao.getRechargetHistoryBysellerId(order.getSeller());
				//如果获取的淘宝支付金额大于商品单价     从新扣冻结金额 修改商品单价
				Double rechargeAmount = payment.doubleValue() * 100;
				if(payment.doubleValue()>goods.getGoodsPrice().doubleValue()){
					
					if(rechargeAmount>amount){//修改金币大于总冻结金额
						if(amount<=0){
							
							UserCenter user  = userCenterDao.getUserBYId(order.getSeller());
							if(user.getCoinsCount()<=rechargeAmount){
						
								goodsDao.updateStatusByUserId(user.getUserName(), 2);
								goodsDao.updatePrice(Long.parseLong(order.getGoodsId()), rechargeAmount/100);
							}else{
								goodsDao.updatePrice(Long.parseLong(order.getGoodsId()), rechargeAmount/100);
							}
							 userCenterDao.updateJinb(order.getSeller(), rechargeAmount);
						}else{
							double keKou = rechargeAmount - amount;
							rechargeDao.updateDjCleanByseller(order.getSeller());
							UserCenter user  = userCenterDao.getUserBYId(order.getSeller());
							if(user.getCoinsCount()<=keKou){
						
								goodsDao.updateStatusByUserId(user.getUserName(), 2);
								goodsDao.updatePrice(Long.parseLong(order.getGoodsId()), rechargeAmount/100);
							}else{
								goodsDao.updatePrice(Long.parseLong(order.getGoodsId()), rechargeAmount/100);
							}
							 userCenterDao.updateJinb(order.getSeller(), keKou);
						}
					}else if(rechargeAmount==amount){//修改金币==总冻结金额
						rechargeDao.updateDjCleanByseller(order.getSeller());
						goodsDao.updatePrice(Long.parseLong(order.getGoodsId()), rechargeAmount/100);
					}else if(rechargeAmount<amount){//修改金币小于总冻结金额
						RechargeHistory recharge = rechargeDao.getRechargetHistoryByGoodsId(Long.parseLong(order.getGoodsId()));
						if(recharge.getRechargeAmount()==rechargeAmount){
							rechargeDao.updateDjRecharge(Long.parseLong(order.getGoodsId()), recharge.getRechargeAmount());
							goodsDao.updatePrice(Long.parseLong(order.getGoodsId()), rechargeAmount/100);
						}else if(recharge.getRechargeAmount()>rechargeAmount){
							rechargeDao.updateDjRecharge(Long.parseLong(order.getGoodsId()), rechargeAmount);
							goodsDao.updatePrice(Long.parseLong(order.getGoodsId()), rechargeAmount/100);
						}else if(recharge.getRechargeAmount()<rechargeAmount){
							System.out.println("1111");
							List<RechargeHistory> lists = rechargeDao.getRechaByseller(order.getSeller());
							double num =0;
							for (RechargeHistory rechargeHistory : lists) {
								System.out.println("------------------------------"+rechargeHistory.getGoodsId());
								if(rechargeHistory.getRechargeAmount()<rechargeAmount){
									System.out.println("22222");
										num = rechargeAmount-rechargeHistory.getRechargeAmount();
										System.out.println("------------------------------"+rechargeHistory.getGoodsId());
										rechargeDao.updateDjCleanByGoodsId(Long.parseLong(rechargeHistory.getGoodsId()));
										rechargeAmount = num;
								}else if(rechargeHistory.getRechargeAmount()==rechargeAmount){
									System.out.println("3333");
									rechargeDao.updateDjCleanByGoodsId(Long.parseLong(rechargeHistory.getGoodsId()));
									 goodsDao.updatePrice(Long.parseLong(rechargeHistory.getGoodsId()), payment.doubleValue());
								}else if(rechargeHistory.getRechargeAmount()>rechargeAmount){
									System.out.println("44444");
									System.out.println("----------------"+rechargeAmount);
									rechargeDao.updateDjRecharge(Long.parseLong(rechargeHistory.getGoodsId()), rechargeAmount);
									 goodsDao.updatePrice(Long.parseLong(rechargeHistory.getGoodsId()), payment.doubleValue());
								}
							}
						}
					 }
				   }else{
					   rechargeDao.updateDjRecharge(Long.parseLong(order.getGoodsId()), goods.getGoodsPrice().doubleValue() * 100);
				   }			
				orderDao.updateOrderStatus(order.getId(), 2);
				orderDao.updateOrderTaobaoAmount(order.getId(), Double.parseDouble(orderTask.getPayment()));
				continue;
			}
			//未支付状态
			if(orderTask.getStatus().equals(TbOrderStatus.WAIT_BUYER_PAY)){
				orderDao.updateOrderStatusDetails(order.getId(), "该订单系统检测并未付款，请付款后再提交正确订单，有疑问请联系商城客服");
				continue;
			}
			//淘宝订单已关闭
			if(orderTask.getStatus().equals(TbOrderStatus.TRADE_CLOSED_BY_TAOBAO)){
				orderDao.updateOrderStatusDetails(order.getId(), "该淘宝订单已显示交易关闭，请重新下单并输入正确订单号");
				continue;
			}
		 }else{
			 orderDao.updateOrderStatusDetails(order.getId(), "订单超时，请联系客服");
				continue;
		 }
	   }
	}
	
	public static OrderTask getOrderParamer(String userName,String tbOrderNo){
		// 公共参数
		HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userName", userName);
        paramsMap.put("tid", tbOrderNo);
        OrderTask orderTask = null;
        if(orderTask==null){
        	orderTask = new OrderTask();
        }
		try {
			String resp = ApiClient
					.sendRequest(
							"http://67.198.169.155/skapi/skQueryOrder",
							null, paramsMap);
			System.out.println("........"+resp);
			JSONObject js = new JSONObject(resp);
			String obj  = js.optString("error_response");
			if(!StringUtils.isEmpty(obj)){
				JSONObject error  = js.getJSONObject("error_response");
				String message = error.get("sub_msg").toString();
				orderTask.setError(false);
				orderTask.setMessage(message);
				return orderTask;
			}
			JSONObject tradeResponse =js.getJSONObject("trade_fullinfo_get_response");
			JSONObject trade = tradeResponse.getJSONObject("trade");
			String sheng = trade.get("receiver_state").toString();//省..... 广东省
			String city = trade.get("receiver_city").toString();//市
			String createTime = trade.get("created").toString();
			String district = "";
			String dis = trade.optString("receiver_district");
			if(!StringUtils.isEmpty(dis)){
				district=trade.get("receiver_district").toString();//区
			}
			String address = trade.get("receiver_address").toString();//详细地址
			String receiver_name = trade.get("receiver_name").toString();//收件人
			System.out.println(sheng+","+city+","+district+","+address+","+receiver_name);
			
			JSONObject orders = trade.getJSONObject("orders");
			
			JSONArray order = orders.getJSONArray("order");
			
			JSONObject order1 = order.getJSONObject(0);
			
			String status = order1.get("status").toString();               //订单状态
			String logistics = "";
//			if(!status.equals("WAIT_BUYER_PAY") && !status.equals("WAIT_SELLER_SEND_GOODS")){
//				logistics = order1.get("logistics_company").toString(); //物流公司
//			}
			String payment = order1.get("payment").toString();             // 付款金额
			
			System.out.println("物流公司:"+logistics+"    订单状态:"+status+"   付款金额"+payment);
			
			
			orderTask.setAddress(sheng+","+city+","+district+","+address);
			orderTask.setReceiverName(receiver_name);
			orderTask.setLogistics(logistics+"");
			orderTask.setStatus(status);
			orderTask.setPayment(payment);
			SimpleDateFormat	sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			orderTask.setCreated(sdf.parse(createTime));
			return orderTask;
		}catch(Exception e){
			System.out.println("淘宝订单号"+tbOrderNo);
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	public static void main(String[] args) throws ParseException {//255993761217534127
		//255993761217534127   代付款
		//236139205939413196    待发货
		//236249670578305872    已发货
		//252842530228950828  退款中
		//252603776894950828  已关闭
		//253879234631164340  已成功
		//成功200
		//连接超时 302
		///调用失败 500
		//签名失败417
		//
		OrderTask order = getOrderParamer("凯伍德贸易有限公司:刷","271417536035915934");
		System.out.println(order);
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		String da = "2018-11-22 07:02:36";
//		
//		long day = 1000 * 60 * 60 * 24;
//		
//		long hours = 1000 * 60 * 60;
//		Date now = new Date();
//
//        Date date = sf.parse(da);
//        
//        long diff = now.getTime() - date.getTime();
//        
//        System.out.println(diff/day);
        
	}

}
