package com.pdatao.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdatao.api.constants.StatusConstants;
import com.pdatao.api.entity.Goods;
import com.pdatao.api.entity.TbGoodsDb;
import com.pdatao.api.service.GoodsService;
import com.pdatao.api.service.TbGoodsDbService;
import com.pdatao.api.util.ApiClient;
import com.pdatao.api.util.CreateUtils;
import com.pdatao.api.util.RsobjectResult;

@RestController
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private TbGoodsDbService goodsDbService;

	@RequestMapping("/getGoods")
	public RsobjectResult<?> getGoods(HttpServletResponse response,Long goodsId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (goodsId == null) {
			return RsobjectResult.define(
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(),
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}

		Goods goods = goodsService.getGoodsBYId(goodsId);
		//关键字 按几率显示
		String[] str = goods.getKeyword().split(";");
		if(str.length>0)
		{
			Integer n =(int)(1+Math.random()*(str.length-1+1));
			goods.setKeywords(str[n-1].split(":")[0]);
			/*for(String s : str)
			{
				int i = goodsService.getKeywords(s.split(":")[0], goodsId);	
				if(i>=Integer.parseInt(s.split(":")[1]))
				{
					continue;
				}else
				{
					goods.setKeywords(s.split(":")[0]);
				}
			}*/
		}
		/*
		String[] str = goods.getKeywords().split(";");
		if(str.length>0)
		{
			List<String[]> list = new ArrayList();
			for (String s : str) {
				String[] ns = s.split(":");
				if(ns.length>1)
				{
					String[] ss ={ns[1],ns[0]};
					list.add(ss);
				}
			}
			if(list.size()>0)
			{
			//冒泡排序
			for(int i=0;i< list.size()-1;i++)
			{
				for(int j=0;j<list.size()-1-i;j++)
				{
					if(Integer.parseInt(list.get(j)[0])>Integer.parseInt(list.get(j+1)[0]))
					{
						String[] k = list.get(j);
						list.set(j, list.get(j+1));
						list.set(j+1, k);
					}
				}
			}
			Integer num=0;
			for(String[] s : list)
			{
				num+=Integer.parseInt(s[0]);
				s[0]=String.valueOf(num);
			}
			Integer n =(int)(1+Math.random()*(num-1+1));
			String index = "";
			for(int i =0;i<list.size();i++)
			{
				if(n<=Integer.parseInt(list.get(i)[0]))
				{
					index = list.get(i)[1];
					break;
				}
			}
			goods.setKeywords(index);
		}
		}
*/
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), goods);

	}
	
	
	@RequestMapping("/getGoodsKeyword")
	public RsobjectResult<?> getGoodsKeyword(HttpServletResponse response,Long goodsId) {
		if (goodsId == null) {
			return RsobjectResult.define(
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(),
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}

		Goods goods = goodsService.getGoodsBYId(goodsId);
		//关键字 按几率显示
		String[] str = goods.getKeyword().split(";");
		if(str.length>0)
		{
			Integer n =(int)(1+Math.random()*(str.length-1+1));
			goods.setKeywords(str[n-1].split(":")[0]);
		}
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), goods.getKeywords());
	}
	
	@RequestMapping("/getGoodsCount")
	public RsobjectResult<?> getGoodsCount(HttpServletResponse response,Long giftId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (giftId == null) {
			return RsobjectResult.define(
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(),
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}

		Integer num = goodsService.getGoodsCount(giftId);

		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), num);

	}

	@RequestMapping("/checkGoodsToken")
	public RsobjectResult<?> checkGoodsToken(HttpServletResponse response,String TBgoodsId,String goodsToken) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(goodsToken==null){
			return RsobjectResult.define(
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(),
					StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		
		String goodsId = returnGoodsId(goodsToken);
		
		if(goodsId==null){
			return RsobjectResult.define("220230", "get TBgoodsId is error");
		}
		System.out.println("TBgoodsId="+TBgoodsId);
		if(goodsId.equals(TBgoodsId)){
			return RsobjectResult.define("200", true);
		}
		return RsobjectResult.define("201", false);
	}
	
	/***
	 * 商品打标
	 * @return
	 */
	@RequestMapping("/goodsDb")
	public RsobjectResult<?> goodsDb(String goodsId,String tbGoodsId,String wwAccount,String keywords){
		
		String resp = dabiao(tbGoodsId, wwAccount, keywords);
		TbGoodsDb goodsDb  = new TbGoodsDb();
		goodsDb.setGoodsId(goodsId);
		goodsDb.setTbGoodsId(tbGoodsId);
		goodsDb.setWwAccount(wwAccount);
		goodsDb.setKeywords(keywords);
		goodsDb.setReturnJson(resp);
		int num = goodsDbService.saveGoodsDb(goodsDb);
		System.out.println(num);
		return RsobjectResult.success();
	}
	
	/***
	 * 未找到商品数量记录
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/updateNotFond")
	public RsobjectResult<?> updateNotFond(Long goodsId){
		int num = goodsService.updateNotFond(goodsId);
		if(num>0){
			return RsobjectResult.success();
		}
		return RsobjectResult.define(StatusConstants.GOODS_UPDATE_NOTFOND_IS_ERROR.getCode(), StatusConstants.GOODS_UPDATE_NOTFOND_IS_ERROR.getMsg());
	}
	
	
	//检测淘宝口令
	public static String returnGoodsId(String goodsToken){
		String [] ips = getIps().split(":");
		// 公共参数
		HashMap<String, String> paramsMap = new HashMap<>();
		paramsMap.put("brandName", "sloekn");
        paramsMap.put("model", "okshow");
        paramsMap.put("deviceId", CreateUtils.getItemID(44));
        paramsMap.put("umt", CreateUtils.getItemID(32));
        paramsMap.put("umidToken", CreateUtils.getItemID(32));
        paramsMap.put("utdid", CreateUtils.getItemID(24));
        paramsMap.put("sid", "");
        paramsMap.put("hid", "");
        paramsMap.put("appVersion", "7.6.0");
        System.out.println("ip="+ips[0]);
        System.out.println("port="+ips[1]);
//        paramsMap.put("ip", ips[0]);
//        paramsMap.put("port", ips[1]);
        goodsToken=goodsToken.replace("€", "￥");
        paramsMap.put("taoCode", goodsToken);
        //http://67.198.189.242:8080/TBNew/api/decryptTaoCode
        //http://67.198.130.5:8080/TBNew/api/decryptTaoCode
		try {
			String resp = ApiClient
					.sendRequest(
							"http://67.198.189.223:8080/TBNew/api/decryptTaoCode",
							null, paramsMap);
			System.out.println("------------------"+resp);
			JSONObject js = new JSONObject(resp);
			JSONObject data = js.getJSONObject("data");
			String url =data.get("url").toString();
			String [] p = url.split("&id=");
			String TBgoodsId = null;
			if(p.length==2){
				String [] o = p[1].split("&sourceType=");
				TBgoodsId=o[0];
			}else{
				 p = url.split(".com/i");
				 String [] o = p[1].split(".htm?");
				 TBgoodsId = o[0];
			}
			return TBgoodsId;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	//任务打标
	public static String dabiao(String goodsId,String wwAccount,String keyword){
		
		StringBuffer sb  = new StringBuffer();
		sb.append("{task:{");
		sb.append("product_id1:"+goodsId+",");
		sb.append("nick1:"+wwAccount+",");
		sb.append("keyword:"+keyword);
		sb.append("}}");
		JSONObject json = null;
		try {
			 json = new JSONObject(sb.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json.toString());
		String resp = ApiClient.sendPost("http://db.sutaohao.com/api/v1/abc/tag", json.toString());
		return resp;
	}
	
	//获取代理ip port
	public static String getIps() {
		String response = ApiClient.sendRequest(
				"http://174.139.156.217:5001/p", null, null);
		String [] ip = response.split("//");
		return ip[1];
	}
	public static void main(String[] args) {
//		String url = "https://a.m.taobao.com/i547210513789.htm?price=20&sourceType=item&sourceType=item&suid=8d20657f-3bd8-4a67-ad4a-05d76e09cfdb&ut_sk=1.W1/jQHscdk0DAJx2QBrbZeCN_21646297_1542086289163.TaoPassword-WeiXin.1&un=8e6dc6e0421063b44b68d5f77a161062&share_crt_v=1&sp_tk=77+lemVPNWJRWXRlUmfvv6U=";
//		String urls= "https://item.taobao.com/item.htm?ut_sk=1.V5V4JjYhWggDAONaTY1m5TAK_21380790_1542090371085.TaoPassword-Weixin.Physical_Screenshots&id=575718963731&sourceType=tao&suid=4429851A-D11E-44CD-A586-9E898BFC37DC&un=23b5fdd4a6e1dc130d92f5aa52e6dceb&share_crt_v=1&sp_tk=77+lZzczdWJRMWdvZFLvv6U=";
//		String [] p = url.split("&id=");
//		if(p.length==1){
//			 p = url.split(".com/i");
//			 System.out.println(p[1]);
//			 String [] o = p[1].split(".htm?");
//			 System.out.println(o[0]);
//		}
//		String [] o = p[1].split("&sourceType=");
//		String TBgoodsId = o[0];
//		System.out.println(TBgoodsId);
		String token = "￥W39WYdTMXv9￥";
		token =token.replace("€", "￥");
		//成功200
		//连接超时 302
		//调用失败 500
		//签名失败417
		System.out.println(returnGoodsId(token));
		

		
	}
}
