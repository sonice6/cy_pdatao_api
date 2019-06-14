package com.pdatao.api.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pdatao.api.constants.StatusConstants;
import com.pdatao.api.entity.CashBack;
import com.pdatao.api.entity.CodeData;
import com.pdatao.api.entity.Member;
import com.pdatao.api.entity.OperationLog;
import com.pdatao.api.entity.Order;
import com.pdatao.api.service.CashBackService;
import com.pdatao.api.service.MemberService;
import com.pdatao.api.service.OperationLogService;
import com.pdatao.api.service.OrderService;
import com.pdatao.api.service.WeChatService;
import com.pdatao.api.util.ApiClient;
import com.pdatao.api.util.HttpUtils;
import com.pdatao.api.util.RsobjectResult;
import com.pdatao.api.util.TimerUtils;

@RestController
public class WeChatController {
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private WeChatService wechatService;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CashBackService cashbackService;
	
	private String bashPath = "/root/pdatao_api/image/member/";
	
	/**
	 * 获取微信信息
	 * @param Code
	 * @return
	 *
	 */
	@RequestMapping("/WechatLogin")
	public RsobjectResult<?> WechatLogin(HttpServletResponse response,String Code){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(Code==null){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		String appid="wx956d346868b108e1";
		String secret = "4b51bcfc5cc205a8db113e0ee8ba637f";
		Member member = wechatService.WechatLogin(Code,appid,secret);
		if(member==null)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_CODE_IS_TIMEOUT.getCode(), StatusConstants.MEMBER_CODE_IS_TIMEOUT.getMsg());
		}else
		{
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member);	
		}
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
	 * 获取微信信息
	 * @param Code
	 * @return
	 *
	 */
	@RequestMapping("/WechatLogins")
	public RsobjectResult<?> WechatLogins(HttpServletResponse response,String Code){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(Code==null){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		String appid="wx972e7a0f31a183be";
		String secret = "b0a063100014495501e851b75a20a4c5";
		Member member = wechatService.WechatLogin(Code,appid,secret);
		if(member==null)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_CODE_IS_TIMEOUT.getCode(), StatusConstants.MEMBER_CODE_IS_TIMEOUT.getMsg());
		}else
		{
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member);	
		}
	}
	
	public static String checkTbAccount(String account){
		HashMap<String, String> paramsMap = new HashMap<>();
		
		paramsMap.put("account", account);
		
		try {
			String resp = ApiClient
					.sendRequest(
							"http://67.198.189.223:8080/Niu/checkAccount",
							null, paramsMap);
			System.out.println(resp);
			return resp;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	@RequestMapping("/setCodeData")
	public int setCodeData(HttpServletResponse response)
	{
		List<CodeData> lstCodeData = memberService.getCodeData();
		int index=0;
		for (int i = 0; i < lstCodeData.size(); i++) {
			if(!lstCodeData.get(i).getTaobaoCode().equals(""))
			{
				String code = checkTbAccount(lstCodeData.get(i).getTaobaoCode());
				if(StringUtils.isEmpty(code)){
					continue;
				}
				String tbcreated=null;
				String good_num = null;
				String total_num =null;
				String vip_info = null;
				try {
					JSONObject js = new JSONObject(code);
					JSONObject user = js.getJSONObject("user");
					tbcreated = user.get("created").toString();
					vip_info = user.get("vip_info").toString();
					JSONObject buyer_credit = user.getJSONObject("buyer_credit");
					total_num=buyer_credit.get("total_num").toString();
					good_num=buyer_credit.get("good_num").toString();
				} catch (JSONException e) {
					System.out.println(e.getMessage());
				}
				if(Integer.parseInt(total_num)<10){
					continue;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        try {
		        	Date created = sdf.parse(tbcreated);
					System.out.println(created);
					int day = TimerUtils.differentDaysByMillisecond(created, new Date());
					if(day<30){
						continue;
					}
					lstCodeData.get(i).setTbCreated(created);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        lstCodeData.get(i).setGoodNum(Integer.parseInt(good_num));
		        lstCodeData.get(i).setTotalNum(Integer.parseInt(total_num));
		        lstCodeData.get(i).setVipInfo(vip_info);
				int warningNum = lstCodeData.get(i).getTotalNum() - lstCodeData.get(i).getGoodNum();
				if(warningNum == 0){
					lstCodeData.get(i).setWarningNum(3);
				}else if(warningNum == 1){
					lstCodeData.get(i).setWarningNum(1);
				}else if(warningNum>=2){
					lstCodeData.get(i).setWarningNum(2);
				}
				index++;
				memberService.updateCodeData(lstCodeData.get(i));
			}
		}
		
		return index;
	}
	
	
	/**
	 * 绑定wx
	 * @param wwAccount
	 * @return
	 *
	 */
	@RequestMapping("/bindWxAccount")
	public RsobjectResult<?> bindWxAccount(HttpServletResponse response,Long id,String wxAccount){
		if(id==null)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
			
		}
		if(wxAccount!=null)
		{
			List<Member> lstWx = wechatService.isExistsWxAccount(wxAccount);
			if(lstWx.size()>0)
			{
				return RsobjectResult.define(StatusConstants.MEMBER_WXACCOUNT_IS_EXISTS.getCode(), StatusConstants.MEMBER_WXACCOUNT_IS_EXISTS.getMsg());
			}

		}
			return bindWxBank(id,null,null,null,wxAccount);
		}
	
	/**
	 * 绑定银行ka
	 * @param wwAccount
	 * @return
	 *
	 */
	@RequestMapping("/bindBankCard")
	public RsobjectResult<?> bindBankCard(HttpServletResponse response,Long id,String bankName,String bankType,String bankAccount){
		if(id==null)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
			
		}
		return bindWxBank(id,bankName,bankType,bankAccount,null);
		
	}
	
	RsobjectResult<?>  bindWxBank(Long id,String bankName,String bankType,String bankAccount,String wxAccount){
		Member member = wechatService.BindBankCard(id, bankName,bankType,bankAccount,wxAccount);
		if(member!=null)
		{
			String s ="";
			try {
				s = new String(URLDecoder.decode(member.getWxNickname()).getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			member.setWxNickname(s);
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member);
		}else
		{
			return RsobjectResult.define(StatusConstants.MEMBER_TBACCOUNT_DATE_IS_ERROR.getCode(), member);
		}
	}
	
	/*******************************计算淘气值***********************************/
	public static String getLevel(String account)
	{
		String cookie="imewweoriw=3%2FsvdVbWVSaEi%2FalCaJmX%2FOcC%2F%2FdlD5IYxIUkhstsRI%3D;WAPFDFDTGFG=%2B4cMKKP%2B8PI%2BL9LMj1%2BC24aH9zk%3D;_w_tb_nick=skv%E9%BB%98%E8%AE%A4;ockeqeudmj=hJJbgHI%3D;uc1=cookie15=WqG3DMC9VAQiUQ%3D%3D&cookie14=UoTZ5OK2LshmNw%3D%3D;unb=3054823147;tracknick=skv%5Cu9ED8%5Cu8BA4;uc3=lg2=W5iHLLyFOGW7aA%3D%3D&vt3=F8dByE0FjuoY2gA8msM%3D&id2=UNDRw904%2FSRsxQ%3D%3D&nk2=EFVdh3PCIg%3D%3D;_cc_=VT5L2FSpdA%3D%3D;lgc=skv%5Cu9ED8%5Cu8BA4;t=c71e8289b5599b239271c16a82529c86;_l_g_=Ug%3D%3D;sg=%E8%AE%A470;cookie17=UNDRw904%2FSRsxQ%3D%3D;skt=f07079d1fe507883;_nk_=skv%5Cu9ED8%5Cu8BA4;cookie1=BqAFbywMoCR%2BanXE8eJx%2BWxQ211n5FbUhEmQQkHL%2BuA%3D;cookie2=1c808b11b827067837d0148cd2622d79;_tb_token_=fe5eb471760ed";
		//account 需要查询的账号
		//请求头信息
		Map<String, String> header=new HashMap<String, String>();
		header.put("Cookie", cookie);//宸茬櫥褰曡处鍙风殑cookie
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.put("Accept-Encoding", "gzip, deflate");
		header.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/544.32 (KHTML, like Gecko) Chrome/43.0.2413.175 Aef/3.38 Qianniu/6.06.02N Safari/547.46");
		header.put("Upgrade-Insecure-Requests","1");
		//url转码
		String acc=urlEncode(account);
		//查询的网页地址
		String url="http://www2.im.alisoft.com/webim/personv6/imUserCard.htm?signmode=im&uid=cntaobao"+acc+"&selfId=cntaobao"+acc+"&site=cntaobao&cache=&edition=1&ver=11.06.02T&imver=11.06.02T&loginId=cntaobao"+acc+"&realuid=cntaobao"+acc;
		String data=HttpUtils.sendRequest(url, header, null, "", "");
		String level = patternStr("var titleStr = '(.+?)';",data);
		String des=patternStr("titleStr \\+= '(.+?)'",data);
		String img=patternStr("innerHTML \\+= \"(.+?)\"",data);
		JSONObject js = new JSONObject();
		try {
			js.accumulate("level", level);
			js.accumulate("des", des);
			js.accumulate("img", img);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(js.toString());
		return level.split(" ")[1].substring(1, level.split(" ")[1].length()-1);
	}
	private static String patternStr(String pattern,String source) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(source);
        String s="";
        if(m.find()){
        	s=m.group(1);
        }
        return s;
	}
	public static String urlEncode(String s){
        String str="";
        try {
            str = URLEncoder.encode(s,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
public static String checkTbAccounts(String account){
		
		HashMap<String, String> paramsMap = new HashMap<>();
		
		paramsMap.put("account", account);
		paramsMap.put("type", "2");
		
		try {
			String resp = ApiClient
					.sendRequest(
							"http://67.198.189.223:8080/Niu/checkAccount",
							null, paramsMap);
			System.out.println(resp);
			return resp;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
//判断绑定旺旺的淘气值
	String bindWwCheck(String account)
	{
		JSONObject js =new JSONObject();
		try {
			js.accumulate("level", getLevel(account));//淘气值
			String ret = checkTbAccounts(account);
			JSONObject user = js.getJSONObject("user");
			String promoted = user.get("promoted_type").toString();
			if(promoted.equals("authentication"))
			{
				js.accumulate("promoted", "0");//实名
			}else
			{
				js.accumulate("promoted", "1");//实名
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return js.toString();
	}
	//检查淘气值等级
		public static Member checkVipLevel(Member member,Integer vip_level)
		{
			Integer level = Integer.parseInt(getLevel(member.getWwAccount())) ;
			if(vip_level==0)
			{
				if(member.getTbPromoted().equals(1))//没有实名
				{
					member.setStatus(2);
					int day = TimerUtils.differentDaysByMillisecond(member.getTbCreated(), new Date());
					if(member.getGoodNum()>=4&&day>=90)
					{
						member.setTbVipLevel(1);
					}else
					{
						member.setTbVipLevel(0);
					}
					member.setTbVip(0);
				}else if(member.getGoodNum()==0)
				{
					member.setStatus(2);
					member.setTbVip(0);
					member.setTbVipLevel(0);
				}else
				{
					member.setStatus(0);
					member.setTbVip(1);	
					member.setTbVipLevel(0);
				}
			}else if(vip_level ==10)
			{
				member.setStatus(0);
				member.setTbVip(2);	
				member.setTbVipLevel(0);
			}else if(vip_level ==20)
			{
				member.setStatus(0);
				member.setTbVip(3);	
				member.setTbVipLevel(0);
			}
			return member;
			/*
			//没有实名的
			if(member.getTbPromoted().equals(1))
			{
				member.setStatus(2);
				int day = TimerUtils.differentDaysByMillisecond(member.getTbCreated(), new Date());
				if(member.getGoodNum()>2||day>60)
				{
					member.setTbVipLevel(1);
				}else
				{
					member.setTbVipLevel(0);
				}
				member.setTbVip(0);
			}else
			{
				
				if(level==0)
				{
					if(member.getTbPromoted().equals(1))
					{
						member.setTbVip(0);	
						int day = TimerUtils.differentDaysByMillisecond(member.getTbCreated(), new Date());
						if(member.getGoodNum()>2||day>60)
						{
							member.setTbVipLevel(1);
						}else
						{
							member.setTbVipLevel(0);
						}
					}else if(member.getGoodNum()==0)
					{
						member.setTbVip(0);		
					}else
					{
						member.setTbVip(1);	
					}
				}
				else if(level>0&&level<=10)
				{
					member.setTbVip(2);
				}else if(level>10&&level<=20)
				{
					member.setTbVip(2);
				}else if(level >=20)
				{
					member.setTbVip(3);
				}
				member.setTbVipLevel(0);
			}
			return member;*/
		}
	/*******************************计算淘气值***********************************/
	
	
	/**
	 * 绑定旺旺
	 * @param wwAccount
	 * @return
	 *
	 */
	@RequestMapping("/bindWWaccount")
	public RsobjectResult<?> bindWWaccount(HttpServletResponse response,Long id,String wwAccount,String wxAccount){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//getLevel("爱英英小屋");
		Integer status ;
		if(wwAccount!=null)
		{
			status=1;
		}else
		{
			status=2;
		}
		if(id==null){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		
		//判断旺旺 微信号是否已经绑定过
		List<Member> lstWw = wechatService.isExistsWwAccount(wwAccount);
		if(lstWw.size()>0)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_WWACCOUNT_IS_EXISTS.getCode(), StatusConstants.MEMBER_WWACCOUNT_IS_EXISTS.getMsg());
		}
		List<Member> lstWx = wechatService.isExistsWxAccount(wxAccount);
		if(lstWx.size()>0)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_WXACCOUNT_IS_EXISTS.getCode(), StatusConstants.MEMBER_WXACCOUNT_IS_EXISTS.getMsg());
		}
		Member mem = new Member();
		Integer vip_level =10;
		if(wwAccount!=null)
		{
			//如果存在就到数据库去找，如果不存在，就调用接口查询
			CodeData codeData = memberService.isExistsWw(wwAccount);
			if(codeData!=null&&codeData.getTaobaoCode()!=null)
			{
				mem.setWwAccount(wwAccount);
				status=3;
				mem.setStatus(0);
				mem.setTbCreated(codeData.getTbCreated());
				mem.setGoodNum(codeData.getGoodNum());
				mem.setTotalNum(codeData.getTotalNum());
				mem.setVipInfo(codeData.getVipInfo());
				mem.setWarningNum(codeData.getWarningNum());
				mem.setTbSex(codeData.getSex());
				mem.setTbPromoted(codeData.getPromotedType());
				if(codeData.getStatus().equals("1")&&codeData.getWechatCode()!=null)
				{
					mem.setWxAccount(codeData.getWechatCode());
				}
			}else
			{
				//判断淘宝号是否有效
				String code = checkTbAccount(wwAccount);
				String codes =checkTbAccounts(wwAccount);
				if(StringUtils.isEmpty(code)){
					return RsobjectResult.define(StatusConstants.MEMBER_TBACCOUNT_IS_EMPT.getCode(), StatusConstants.MEMBER_TBACCOUNT_IS_EMPT.getMsg());
				}
				
				String tbcreated=null;
				String good_num = null;
				String total_num =null;
				String vip_info = null;
				Integer sex =null;
				Integer promoted =null;
				try {
					JSONObject js = new JSONObject(code);
					JSONObject user = js.getJSONObject("user");
					JSONObject jss = new JSONObject(codes);
					JSONObject users = jss.getJSONObject("user");
					tbcreated = user.get("created").toString();
					vip_info = user.get("vip_info").toString();
					vip_level = user.getInt("vip_level");
					JSONObject buyer_credit = user.getJSONObject("buyer_credit");
					total_num=buyer_credit.get("total_num").toString();
					good_num=buyer_credit.get("good_num").toString();
					
					if(user.get("sex").equals("m"))
					{
						sex = 0;
					}else
					{
						sex = 1;
					}
					if(users.get("promoted_type").toString().equals("authentication"))
					{
						promoted = 0;
					}else
					{
						promoted = 1;
					}
				} catch (JSONException e) {
					System.out.println(e.getMessage());
				}
				if(Integer.parseInt(total_num)<10){
					return RsobjectResult.define(StatusConstants.MEMBER_TBACCOUNT_TOTAL_NUM_ERROR.getCode(), StatusConstants.MEMBER_TBACCOUNT_TOTAL_NUM_ERROR.getMsg());
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        try {
		        	Date created = sdf.parse(tbcreated);
					System.out.println(created);
					int day = TimerUtils.differentDaysByMillisecond(created, new Date());
					if(day<30){
						return RsobjectResult.define(StatusConstants.MEMBER_TBACCOUNT_DATE_IS_ERROR.getCode(), StatusConstants.MEMBER_TBACCOUNT_DATE_IS_ERROR.getMsg());
					}
					mem.setTbCreated(created);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        mem.setGoodNum(Integer.parseInt(good_num));
				mem.setTotalNum(Integer.parseInt(total_num));
				mem.setVipInfo(vip_info);
				mem.setTbSex(sex);
				mem.setTbPromoted(promoted);
				int warningNum = mem.getTotalNum() - mem.getGoodNum();
				if(warningNum == 0){
					mem.setWarningNum(3);
				}else if(warningNum == 1){
					mem.setWarningNum(1);
				}else if(warningNum>=2){
					mem.setWarningNum(2);
				}
				mem.setWwAccount(wwAccount);
				mem.setWxAccount(wxAccount);
			}
		}
		//参数赋值
		mem.setId(id);
		//判断淘气值
		mem = checkVipLevel(mem,vip_level);
		Member member = wechatService.BindWw(mem,status);
		addLog("买手管理",member.getWwAccount(),member.getId(),"买手id="+member.getId()+";tbvip="+member.getTbVip()+";status="+member.getStatus());
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member);
	}
	
	
	@RequestMapping(value="/uploadImage")
    public RsobjectResult<?>  uploadImage(HttpServletRequest request,HttpServletResponse response,MultipartHttpServletRequest multiRequest,Long Id){

    	Calendar cal = Calendar.getInstance();
    	String data = cal.get(cal.YEAR)+ "" + (cal.get(cal.MONTH)+1) + cal.get(cal.DATE);
    	String goPath  =data+ "/";
    	String path = bashPath + goPath;
    	Long times = System.currentTimeMillis();
    	String rsp = null;
    	System.out.println("储存路径："+path);
    	File pFile = new File(path);
		if(!pFile.exists())
			pFile.mkdirs();
		MultipartFile file = multiRequest.getFile("file");
		if(file.getSize()>0){//<(1024*1024)
			String name  =  file.getOriginalFilename();
			
			String names = name.substring(name.indexOf("."));
			
			String imageName = times + names;
			 
			String returnPath = goPath+imageName;
			
			String filePath = path+imageName;
			
			try {
				FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(filePath));
			} catch (Exception e) {
				rsp = "上传失败";
			}
			rsp = returnPath;
		}else{
			rsp = "文件超过大小";
		}
		System.out.println("***************************"+rsp);
		memberService.updateMemberImage(Id, "http://129.204.167.70:8080/files/"+rsp+"|");
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), 1);
    }
	
	//小程序
	@RequestMapping(value="/returnOrderRedCash")
    public RsobjectResult<?>  returnOrderRedCash(HttpServletResponse response,String name,String code,String taobaoOrder){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Member member = wechatService.WechatApp(name,code);
		if(member!=null)
		{
			Order order = orderService.getOrderByTaobao(taobaoOrder); 
			if(order!=null)
			{
				if(order.getStatus()==2&&order.getBuyer()==member.getId())
				{
					orderService.updateOrderStatus(order.getId(), 4);
					System.out.println("返款"+order.getTaobaoOrder());
					//返款成功插入红包记录
					CashBack cashBack = new CashBack();
					cashBack.setCashbackCode(System.currentTimeMillis()+"");
					cashBack.setCashbackOrder(order.getTaobaoOrder());
					cashBack.setCashbackBuyer(order.getBuyer()+"");
					cashBack.setCashbackAmount(order.getTaobaoAmount());
					cashbackService.CashBackAdd(cashBack);
					System.out.println("返现红包流水："+cashBack.getCashbackCode());
					return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), cashBack.getCashbackCode());			
				}else
				{
					return RsobjectResult.define(StatusConstants.MEMBER_ORDER_IS_ERROR.getCode(), 1);
				}
			}else
			{
				return RsobjectResult.define(StatusConstants.MEMBER_TAOBAOORDER_IS_NOT_FOUNT.getCode(), 1);
			}
		}else
		{
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_EXIST.getCode(), 1);	
		}
	}
	
	/**
	 * 绑定账号
	 * @param wwAccount
	 * @return
	 *
	 */
	@RequestMapping("/bindOldAccount")
	public RsobjectResult<?> bindOldAccount(HttpServletResponse response,Long id,String Phone,String wwAccount){
		//查询现在帐号
		Member memberNew = memberService.getMemberById(id);
		//查询老账号
		Member memberOld = memberService.getMemberByPhone(Phone);
		if(StringUtils.isEmpty(memberNew)||StringUtils.isEmpty(memberOld))
		{
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_EXIST.getCode(), StatusConstants.MEMBER_USER_IS_EXIST.getMsg());
		}else
		{
			memberOld.setWxid(memberNew.getWxid());
			memberOld.setWxImage(memberNew.getWxImage());
			memberOld.setWxNickname(memberNew.getWxNickname());
			memberOld.setWxUnionid(memberNew.getWxUnionid());
			memberService.updateAccount(memberOld);
			memberService.deleteMember(memberNew.getId());
			return bindWWaccount(response, memberOld.getId(), wwAccount, null);
		}
	}

}
