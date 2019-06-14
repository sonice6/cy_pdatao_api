package com.pdatao.api.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.Utils;
import com.pdatao.api.constants.GlobalFinal;
import com.pdatao.api.constants.TbOrderStatus;
import com.pdatao.api.dao.GoodsMapper;
import com.pdatao.api.dao.MemberMapper;
import com.pdatao.api.dao.OrderMapper;
import com.pdatao.api.dao.RechargeHistoryMapper;
import com.pdatao.api.dao.ShopMapper;
import com.pdatao.api.dao.TbUserMapper;
import com.pdatao.api.dao.UserCenterMapper;
import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.Order;
import com.pdatao.api.entity.OrderTask;
import com.pdatao.api.entity.RechargeHistory;
import com.pdatao.api.entity.Shop;
import com.pdatao.api.entity.TbUser;
import com.pdatao.api.entity.UserCenter;
import com.pdatao.api.service.CheckOrderTimers;
import com.pdatao.api.util.ApiClient;

@Component
public class CheckOrderTimersImpl implements CheckOrderTimers {
	

	
	@Autowired
    private ObjectMapper mapper ;
    
	@Autowired
	private OrderMapper orderDao;
	
	@Autowired
	private TbUserMapper tbUserDao;
	
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
	private static  final String BASE_URL = "http://67.198.130.5:8080";

	public void checkOrder(Order order){
			System.out.println("开始执行订单验证-------------");
			System.out.println("+++++"+order.getShopId());
			Shop shop = shopDao.getShopById(order.getShopId());
			OrderTask orderTask = getOrderParamer(shop.getTbAccount(),order.getTaobaoOrder());
			long d = 1000 * 60 * 60 * 24;
			long h = 1000 * 60 * 60;
			
			if(orderTask==null){//自动验证订单异常 改为手动验证
				orderDao.updateTaskOrderStatus(order.getId());
				return;
			}
			if(!orderTask.isError()){
				orderDao.updateOrderStatusDetails(order.getId(), orderTask.getMessage());
				return;
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
			if(orderTask.getStatus().equals(TbOrderStatus.WAIT_SELLER_SEND_GOODS)|| orderTask.getStatus().equals(TbOrderStatus.WAIT_BUYER_CONFIRM_GOODS)){
				System.out.println("----0"+order.getGoodsId());
				Goods goods = goodsDao.getGoodsBYId(Long.parseLong(order.getGoodsId()));
				if(goods==null){
					System.out.println("商品为空");
					System.out.println("订单编号=="+order.getId());
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
				return;
			}
			//未支付状态
			if(orderTask.getStatus().equals(TbOrderStatus.WAIT_BUYER_PAY)){
				orderDao.updateOrderStatusDetails(order.getId(), "该订单系统检测并未付款，请付款后再提交正确订单，有疑问请联系商城客服");
				return ;
			}
			//淘宝订单已关闭
			if(orderTask.getStatus().equals(TbOrderStatus.TRADE_CLOSED_BY_TAOBAO)){
				orderDao.updateOrderStatusDetails(order.getId(), "该淘宝订单已显示交易关闭，请重新下单并输入正确订单号");
				return ;
			}
	     }else{
			 orderDao.updateOrderStatusDetails(order.getId(), "订单超时，请联系客服");
				return;
		 }
	}
	
	public OrderTask getOrderParamer(String userName,String tbOrderNo){
		// 公共参数
		HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userName", userName);
        paramsMap.put("tid", tbOrderNo);
        OrderTask orderTask = null;
        if(orderTask==null){
        	orderTask = new OrderTask();
        }
		try {
			//String resp = ApiClient
				//	.sendRequest(
					//		"http://67.198.169.155/skapi/skQueryOrder",
						//	null, paramsMap);
			String resp = skQueryOrder(userName,tbOrderNo);
			System.out.println("........"+resp);
			JSONObject js = new JSONObject(resp);
			System.out.println("-----"+js);
			String obj  = js.optString("error_response");
			if(!StringUtils.isEmpty(obj)){
				JSONObject error  = js.getJSONObject("error_response");
				String message = error.get("sub_msg").toString();
				orderTask.setError(false);
				orderTask.setMessage(message);
				return  orderTask;
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
	
    public String skQueryOrderss(String userName,String tid) throws org.json.JSONException {


        // 请求结果
        ObjectNode result = mapper.createObjectNode();
        ObjectNode data = mapper.createObjectNode();
        // 查找用户及设备，不存在时关联一个设备
        UserCenter u = userCenterDao.selectByUsername(userName);

        // 若无用户信息，则一般是生成设备不足
        if (u==null) {
            result.put("code", 500);
            result.put("msg", "该用户未登陆");
            return result.toString();
        }else{


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));


            try {

                String r = "";
                ReadContext ctx = null;
                for(int retries =0; ; retries++){
                    try {
                       MultiValueMap<String, String> postParams = new LinkedMultiValueMap<String, String>();

                        //String[] proxy = GlobalFinal.getProxyForUser(u);
                        //postParams.remove("ip");
                        //postParams.remove("port");
                        //String session = new org.json.JSONObject(u.getJson3()).getJSONObject("auth_refresh_post_response").getString("client_access_token");
                        postParams.add("ip", "");
                        postParams.add("port", "");

                        // 公共参数
                        postParams.add("session", "");
                        postParams.add("deviceId", "");
                        postParams.add("uid", u.getId().toString());
                        postParams.add("tid", tid);

                        r = restTemplate.postForObject(BASE_URL + "/QianNiu/api/queryOrder", postParams, String.class);
                        System.out.println("==> query order " + tid + " user" + userName  + postParams.toString() + "return \n " + r);
                        Boolean refreshSession = false;
                        org.json.JSONObject queryResult = new org.json.JSONObject(r);
                        try {
                            if(queryResult.has("error_response") && (queryResult.getJSONObject("error_response").getInt("code") == 27 ||  queryResult.getJSONObject("error_response").getInt("code") == 53)){
                                try {
                                    MultiValueMap<String, String> postParams1 = new LinkedMultiValueMap<String, String>();
                                    postParams1.clear();
                                   // org.json.JSONObject userObject = loginPostResponse.getJSONObject("user");
                                    postParams1.add("ip", "");
                                    postParams1.add("port", "");
                                    postParams1.add("session", "");

                                    postParams1.add("nick", u.getUserName());
                                    postParams1.add("subUid", String.valueOf(u.getId().toString()));
									postParams1.add("uid", String.valueOf(u.getId().toString()));
                                    postParams1.add("subNick", u.getUserName());
                                    postParams1.add("usession", "");
                                    postParams1.add("deviceId", "");
                                    String r2 = restTemplate.postForObject(BASE_URL + "/QianNiu/api/refresh", postParams1, String.class);
                                    System.out.println("==> refresh with " + postParams1.toString() + "return \n " + r2);
                                    //u.setJson3(r2);
                                    //tbUserService.updateByPrimaryKeySelective(u);
                                    refreshSession = true;
                                } catch (RestClientException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                        if(refreshSession){
                            continue;
                        }


//                        ctx = parseContext.parse(r);
//                        String  oscode = ctx.read("$.code",String.class);
//                        if(oscode.equals("302")){
//                            throw new IOException("代理超时");
//                        }
                        return r;
//                        break;
                    } catch (Exception e) {
                        // 失败重试
                        if(retries <2){
                            // 请求失败，刷新代理IP

                            continue;
                        }else{
                            break;
                        }
                    }
                }

                // 若无ctx ,则抛出异常，走异常失败流程
                if(ctx == null){
                    throw new IOException("请求接口失败");
                }
                

            } catch (Exception e) {
                result.put("code", 500);
                result.put("msg", e.toString());
                e.printStackTrace();
            }



        }
        	return result.toString();
    }
    
    public  static void main(String[] args) throws ParseException, IOException {
    	//String str  = skQueryOrder("困龙翔天","397955872103749663");
    }
    
    
    public String skQueryOrder(String userName,String tid) {


        // 请求结果
        ObjectNode result = mapper.createObjectNode();
        ObjectNode data = mapper.createObjectNode();
        // 查找用户及设备，不存在时关联一个设备
        TbUser u = tbUserDao.selectByUsername(userName);

        // 若无用户信息，则一般是生成设备不足
        if (null == u || StringUtils.isEmpty(u.getJson2())) {
            result.put("code", 500);
            result.put("msg", "该用户未登陆");
            return result.toString();
        }else{


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));


            try {

                String r = "";
                ReadContext ctx = null;
                for(int retries =0; ; retries++){
                    try {
                        // 请求时准备代理
                        org.json.JSONObject j = new org.json.JSONObject(u.getJson2());
                        org.json.JSONObject loginPostResponse = new org.json.JSONObject(j.getString("data") ).getJSONObject("login_post_response");
                        String clientAccessToken  = loginPostResponse.getJSONObject("jdy_auth").getString("client_access_token");

                        data.put("client_access_token", clientAccessToken);

                        String session = new org.json.JSONObject(u.getJson3()).getJSONObject("auth_refresh_post_response").getString("client_access_token");
                        MultiValueMap<String, String> postParams = new LinkedMultiValueMap<String, String>();

                        String[] proxy = GlobalFinal.getProxyForUser(u);
                        postParams.remove("ip");
                        postParams.remove("port");
                        postParams.add("ip", proxy[0]);
                        postParams.add("port", proxy[1]);

                        // 公共参数
                        postParams.add("session", session);
                        postParams.add("deviceId", u.getDeviceId());
                        postParams.add("uid", loginPostResponse.getJSONObject("user").getString("user_id"));
                        postParams.add("tid", tid);

                        r = restTemplate.postForObject(BASE_URL + "/QianNiu/api/queryOrder", postParams, String.class);
                        System.out.println("==> query order " + tid + " user" + userName  + postParams.toString() + "return \n " + r);
                        Boolean refreshSession = false;
                        org.json.JSONObject queryResult = new org.json.JSONObject(r);
                        try {
                            if(queryResult.has("error_response") && (queryResult.getJSONObject("error_response").getInt("code") == 27 ||  queryResult.getJSONObject("error_response").getInt("code") == 53)){
                                try {
                                    MultiValueMap<String, String> postParams1 = new LinkedMultiValueMap<String, String>();
                                    postParams1.clear();
                                    org.json.JSONObject userObject = loginPostResponse.getJSONObject("user");
                                    postParams1.add("ip", proxy[0]);
                                    postParams1.add("port", proxy[1]);
                                    postParams1.add("session", clientAccessToken);

                                    postParams1.add("nick", userObject.getString("main_nick"));
                                    try {
                                        postParams1.add("subUid", userObject.getString("user_id"));
                                        postParams1.add("uid", userObject.getString("main_user_id"));
                                    } catch (org.json.JSONException e) {
                                        e.printStackTrace();
                                    }
                                    postParams1.add("subNick", userObject.getString("user_name"));
                                    postParams1.add("usession", loginPostResponse.getString("usession_id"));
                                    postParams1.add("deviceId", u.getDeviceId());
                                    String r2 = restTemplate.postForObject(BASE_URL + "/QianNiu/api/refresh", postParams1, String.class);
                                    System.out.println("==> refresh with " + postParams1.toString() + "return \n " + r2);
                                    u.setJson3(r2);
                                    tbUserDao.updateByPrimaryKeySelective(u);
                                    refreshSession = true;
                                } catch (org.json.JSONException e) {
                                    e.printStackTrace();
                                } catch (RestClientException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                        if(refreshSession){
                            continue;
                        }


//                        ctx = parseContext.parse(r);
//                        String  oscode = ctx.read("$.code",String.class);
//                        if(oscode.equals("302")){
//                            throw new IOException("代理超时");
//                        }
                        return r;
//                        break;
                    } catch (Exception e) {
                        // 失败重试
                        if(retries <2){
                            // 请求失败，刷新代理IP

                            continue;
                        }else{
                            break;
                        }
                    }
                }

                // 若无ctx ,则抛出异常，走异常失败流程
                if(ctx == null){
                    throw new IOException("请求接口失败");
                }
                

            } catch (Exception e) {
                result.put("code", 500);
                result.put("msg", e.toString());
                e.printStackTrace();
            }



        }

        //第一步 根据ip 获取城市 入库 并保存队列 用户对应代理ip
//        if (action.equals("login")) {
//            //省份 城市入库
////
//        } else {
//            result.put("code", 500);
//            result.put("msg", "无效的参数");
//            return result;
//        }
        return result.toString();
    }
}
