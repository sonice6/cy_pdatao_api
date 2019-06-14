package com.pdatao.api.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pdatao.api.constants.StatusConstants;
import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.GoodsDetail;
import com.pdatao.api.entity.OperationLog;
import com.pdatao.api.entity.Order;
import com.pdatao.api.entity.OrderHistory;
import com.pdatao.api.service.CheckOrderTimers;
import com.pdatao.api.service.GoodsDetailService;
import com.pdatao.api.service.GoodsService;
import com.pdatao.api.service.MemberService;
import com.pdatao.api.service.OperationLogService;
import com.pdatao.api.service.OrderHistroyService;
import com.pdatao.api.service.OrderService;
import com.pdatao.api.util.RedisUtil;
import com.pdatao.api.util.RsobjectResult;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderHistroyService orderHistroyService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private GoodsService goodsService;
	
	private GoodsDetailService goodsDetailService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CheckOrderTimers checkOrderTimesService;
	
	@Autowired
	private OperationLogService operationLogService;
	
	
	/**
	 * 获取订单信息
	 * 
	 * @param request
	 * @param userCenter
	 * @return
	 */
	@RequestMapping(value="/getOrders",method=RequestMethod.GET)
	public RsobjectResult<?> getOrders(HttpServletResponse response,Integer userId,String status){
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<Order> list = orderService.getOrder(userId,status);
		
		return RsobjectResult.define("200", list);
	}
	
	void addLog(String type,String name,Long id,String text)
	{
		OperationLog operationLog =new OperationLog();
		operationLog.setCreateDate(new Date());
		operationLog.setCreateUser(name);
		operationLog.setModularId(id);
		operationLog.setModular(text);
		operationLog.setType(type);
		operationLogService.saveOperationLog(operationLog);
	}
	
	/**
	 * 增加或者修改订单
	 * 
	 * @param request
	 * @param userCenter
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody RsobjectResult<?> saveOrUpdateOrder(HttpServletResponse response,HttpServletRequest request, Order order) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (order != null) {
			boolean b = false;
			if(order.getId()==null){
				//判断买手状态
				Integer num = orderService.getByuerStatus(order.getBuyer());
				if(num==2)
				{
					return RsobjectResult.define(StatusConstants.MEMBER_STATUA_IS_NOT_USER.getCode(), StatusConstants.MEMBER_STATUA_IS_NOT_USER.getMsg());
				}
				b = true;
				order.setOrderNo(order.getGoodsId()+ System.currentTimeMillis()+"");
			}
			Integer flag =  orderService.saveOrUpdateOrder(order);
			String text  = "订单管理：goodsId="+order.getGoodsId()+";订单id="+order.getId();
			addLog("添加or修改订单:"+String.valueOf(order.getStatus()),String.valueOf(order.getBuyer()),order.getBuyer(),text);
			if(flag>0)
			{
				if(flag==23){
					return RsobjectResult.define("102", "商品数量不足");
				}else if(flag==24){
					return RsobjectResult.define("203", "账户可领取次数不足");
				}else if(flag==25){
					return RsobjectResult.define("204", "有订单未完成");
				}else if(flag==26)
				{
					return RsobjectResult.define("205", "当前没有可用任务");
				}
				
				if(b){
					
					OrderHistory orderHistory = new OrderHistory();
					orderHistory.setType(0);
					orderHistory.setBuyer(order.getBuyer());
					orderHistory.setSeller(order.getSeller());
					orderHistory.setGiftId(Long.parseLong(order.getGiftId()));
					orderHistory.setGoodsId(Long.parseLong(order.getGoodsId()));
					orderHistory.setNum(1);
					orderHistory.setOrderNo(Long.parseLong(order.getOrderNo()));
					orderHistory.setShopId(order.getShopId());
					orderHistroyService.saveOrderHistory(orderHistory);
					
					//扣除关键字
					Goods goods = goodsService.getGoodsBYId(Long.parseLong(order.getGoodsId()));
					if(goods!=null)
					{
						String[] str = goods.getKeyword().split(";");
						String strKey="";
						for(String s : str)
						{
							if(s.split(":")[0].equals(order.getKeywords()))
							{
								if(Integer.parseInt(s.split(":")[1])==1)
								{
									continue;
								}else
								{
									strKey+=s.split(":")[0]+":"+(Integer.parseInt(s.split(":")[1])-1)+";";
								}
							}else
							{
								strKey+=s+";";
							}
						}
						if(strKey!="")
						{
							strKey=strKey.substring(0, strKey.length()-1);
						}
						goodsService.updateKeyword(goods.getId(), strKey);
					}

				}
				return RsobjectResult.define("200", "成功");
			}
			else
			{
				if(flag==-1)
				{
					return RsobjectResult.define("101", "订单号不匹配");	
				}else
				{
					return RsobjectResult.define("103", "订单号不能重复");
				}
				
			}
			
		}
		return RsobjectResult.define("200", null);
	}
	
	/**
	 * 增加或者修改订单
	 * 
	 * @param request
	 * @param userCenter
	 * @return
	 */
	@RequestMapping(value = "/updateOrderCode", method = RequestMethod.POST)
	public @ResponseBody RsobjectResult<?> updateOrderCode(HttpServletResponse response,HttpServletRequest request, Order order) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (order != null) {
			int num = orderService.updateOrderCode(order);
			String text  = "订单管理：goodsId="+order.getGoodsId()+";订单id="+order.getId();
			addLog("修改订单:"+String.valueOf(order.getStatus()),String.valueOf(order.getId()),order.getId(),text);
			if(num<0)
			{
				if(num==-2)
				{
					return RsobjectResult.define("105", "订单号已存在");	
				}else if(num==-3)
				{
					return RsobjectResult.define("106", "订单号后6位不匹配");	
				}else
				{
					return RsobjectResult.define("101", "订单号不匹配");
				}
				
			}else
			{
				//自动验证一次订单
				Order orders = orderService.getOrderById(order.getId());
				//如果是取消订单，平均分配的数量要释放
				if(orders.getStatus()==5){
					Goods goods = goodsService.getGoodsBYId(Long.parseLong(orders.getGoodsId()));
					if(goods.getReleaseType()==0){
						GoodsDetail goodsDetail = goodsDetailService.getGoodsdetail(Long.parseLong(orders.getGoodsId()), new Date());
						
						if(goodsDetail!=null){
							goodsDetailService.jiaGoodsDetailNum(goodsDetail.getId(), 1);
						}
					}
				}
				checkOrderTimesService.checkOrder(orders);
				return RsobjectResult.define("200", "修改订单成功");
			}
			
		}
		return RsobjectResult.define("201", "参数不能为空");
	}
	
	
	/***
	 * 修改订单状态
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/updateOrderStatus")
	public RsobjectResult<?> updateOrderStatus(Long orderId,Integer status){
		
		
		int num = orderService.updateOrderStatus(orderId, status);
		String text  = "订单管理：订单id="+orderId;
		addLog("修改订单状态："+String.valueOf(status), String.valueOf(orderId), orderId,text);
		if(num>0){
			return RsobjectResult.success();
		}
		
		return RsobjectResult.define(StatusConstants.ORDER_CANCEL_ORDER_IS_ERROR.getCode(), StatusConstants.ORDER_CANCEL_ORDER_IS_ERROR.getMsg());
	}

	/***
	 * 取消订单
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	public RsobjectResult<?> cancelOrder(Long userId,Long orderId){
		
		
		memberService.updateMemberTrueNum(userId);
		
		int num = orderService.updateOrderStatus(orderId, 5);
		String text  = "订单管理：订单id="+orderId;
		addLog("取消订单",String.valueOf(orderId),orderId,text);
		if(num>0){
			orderService.updateGoodsStock(orderId,0);
			orderService.updateGift(orderId,0);
			orderService.updateOrderHistory(orderId,0);
			return RsobjectResult.success();
		}
		
		return RsobjectResult.define(StatusConstants.ORDER_CANCEL_ORDER_IS_ERROR.getCode(), StatusConstants.ORDER_CANCEL_ORDER_IS_ERROR.getMsg());
	}
}
