package com.pdatao.api.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdatao.api.constants.StatusConstants;
import com.pdatao.api.entity.CashBack;
import com.pdatao.api.entity.Member;
import com.pdatao.api.entity.Order;
import com.pdatao.api.service.CashBackService;
import com.pdatao.api.service.MemberService;
import com.pdatao.api.service.OrderService;
import com.pdatao.api.service.WeChatService;
import com.pdatao.api.util.ApiClient;
import com.pdatao.api.util.AppMD5Utils;
import com.pdatao.api.util.HttpUtils;
import com.pdatao.api.util.IpAddressUtils;
import com.pdatao.api.util.PhoneUtils;
import com.pdatao.api.util.RedisUtil;
import com.pdatao.api.util.RsobjectResult;
import com.pdatao.api.util.TimerUtils;

@RestController
public class MemberController {
	
	@Autowired
	private WeChatService wechatService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CashBackService cashService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public RsobjectResult<?> login(HttpServletResponse response,String userName,String password,String token){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(StringUtils.isEmpty(userName) && StringUtils.isEmpty(password)&&StringUtils.isEmpty(token)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		Member member = memberService.getMember(userName, password);
		
		
		if(member!=null){
			String s ="";
			try {
				s = new String(URLDecoder.decode(member.getWxNickname()).getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			redisUtil.set(token, member, 10000);
			member.setWxNickname(s);
			if(member.getStatus()==1){
				return RsobjectResult.define(StatusConstants.MEMBER_USER_ACCOUNT_IS_LOCK.getCode(), StatusConstants.MEMBER_USER_ACCOUNT_IS_LOCK.getMsg());
			}
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(),member);
		}
		return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NOT_FOUND.getCode(), StatusConstants.MEMBER_USER_IS_NOT_FOUND.getMsg());
	}
	
	@RequestMapping("/loginOut")
	public RsobjectResult<?> loginOut(HttpServletResponse response,String token){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(StringUtils.isEmpty(token)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
//		redisUtil.del(token);
		
		return RsobjectResult.success();
	}
	
	@RequestMapping("/getMember")
	public RsobjectResult<?> getMember(HttpServletResponse response,String token){
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(StringUtils.isEmpty(token)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		Object obj =  redisUtil.get(token);
		if(obj==null){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NO_LOGIN.getCode(), StatusConstants.MEMBER_USER_IS_NO_LOGIN.getMsg());
		}
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), (Member)obj);
	}
	
	
	@RequestMapping("/register")
	public RsobjectResult<?> register(HttpServletRequest request,Member member){
		if(StringUtils.isEmpty(member)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		
		//判断旺旺 微信号是否已经绑定过
		List<Member> lstWw = wechatService.isExistsWwAccount(member.getWwAccount());
		if(lstWw.size()>0)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_WWACCOUNT_IS_EXISTS.getCode(), StatusConstants.MEMBER_WWACCOUNT_IS_EXISTS.getMsg());
		}
		List<Member> lstWx = wechatService.isExistsWxAccount(member.getWxAccount());
		if(lstWx.size()>0)
		{
			return RsobjectResult.define(StatusConstants.MEMBER_WXACCOUNT_IS_EXISTS.getCode(), StatusConstants.MEMBER_WXACCOUNT_IS_EXISTS.getMsg());
		}
		String code = checkTbAccount(member.getUserName(),0);
		String codes = checkTbAccount(member.getUserName(),1);
		
		if(StringUtils.isEmpty(code)){
			return RsobjectResult.define(StatusConstants.MEMBER_TBACCOUNT_IS_EMPT.getCode(), StatusConstants.MEMBER_TBACCOUNT_IS_EMPT.getMsg());
		}

		Member mber = memberService.getMemberByuserName(member.getUserName());
		
		if(mber!=null){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_EXIST.getCode(), StatusConstants.MEMBER_USER_IS_EXIST.getMsg());
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
			tbcreated = user.get("created").toString();
			vip_info = user.get("vip_info").toString();
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
			JSONObject jss = new JSONObject(codes);
			JSONObject users = jss.getJSONObject("user");
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
			member.setTbCreated(created);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		member.setGoodNum(Integer.parseInt(good_num));
		member.setTotalNum(Integer.parseInt(total_num));
		member.setVipInfo(vip_info);
		member.setTbSex(sex);
		member.setTbPromoted(promoted);
		int warningNum = member.getTotalNum() - member.getGoodNum();
		if(warningNum == 0){
			member.setWarningNum(3);
		}else if(warningNum == 1){
			member.setWarningNum(1);
		}else if(warningNum>=2){
			member.setWarningNum(2);
		}
		if(member.getPhoneNo().equals("")){
			member.setPhoneNo("");
		}
		if(member.getEmail().equals("")){
			member.setEmail("");
		}
		String ip = IpAddressUtils.getIpAddress(request);
		member.setIp(ip);
		member.setWxAccount(member.getPhoneNo());
		member.setWwAccount(member.getUserName());
		member = checkVipLevel(member);
		int num = memberService.register(member);
		
		if(num>0){
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), num);
		}
		
		return RsobjectResult.define(StatusConstants.MEMBER_USER_REGIST_IS_ERROR.getCode(), StatusConstants.MEMBER_USER_REGIST_IS_ERROR.getMsg());
		
	}
	
	/***
	 * 网站 手机号注册
	 */
	@RequestMapping("/phoneRegist")
	public RsobjectResult<?> phoneRegist(HttpServletRequest request,String phoneNo,String password){
		if(StringUtils.isEmpty(password) && StringUtils.isEmpty(phoneNo)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		//验证是否是正确的手机号
		if(!PhoneUtils.isPhone(phoneNo)){
			return RsobjectResult.define(StatusConstants.MEMBER_PHONE_NUM_NOT_RULE.getCode(), StatusConstants.MEMBER_PHONE_NUM_NOT_RULE.getMsg());
		}
		
		Member member = memberService.getMemberByPhone(phoneNo);
		//手机号是否被注册
		if(member!=null){
			return RsobjectResult.define(StatusConstants.MEMBER_PHONE_NUM_IS_EXIST.getCode(),StatusConstants.MEMBER_PHONE_NUM_IS_EXIST.getMsg());
		}
		
		String ip = IpAddressUtils.getIpAddress(request);
		if(!StringUtils.isEmpty(ip)){
			Integer ipCount = memberService.getIpCount(ip);
			//同一个ip 一天不能超过50次注册
			if(ipCount>50){
				return RsobjectResult.define(StatusConstants.MEMBER_IP_COUNT_TOO_LONG.getCode(), StatusConstants.MEMBER_IP_COUNT_TOO_LONG.getMsg());
			}
		}
		Member addMember = new Member();
		addMember.setPhoneNo(phoneNo);
		addMember.setUserName(phoneNo);
		addMember.setPassword(AppMD5Utils.getMD5(password));
		addMember.setWwAccount("");
		addMember.setEmail("");
		addMember.setStatus(0);
		addMember.setTotalNum(0);
		addMember.setGoodNum(0);
		addMember.setVipInfo("");
		addMember.setWarningNum(0);
		addMember.setWxid("");
		addMember.setWxNickname("");
		addMember.setWxImage("");
		addMember.setIp(ip);
		
		memberService.WeRegister(addMember);
		return RsobjectResult.success(addMember);
	}
	
	//忘记密码
	@RequestMapping("/forgetPassword")
	public RsobjectResult<?> forgetPassword(String userName,String newPassword){
		if(StringUtils.isEmpty(userName)&& StringUtils.isEmpty(newPassword)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		Member member = memberService.getMemberByuserName(userName);
		
		if(member==null){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NULL.getCode(), StatusConstants.MEMBER_USER_IS_NULL.getMsg());
		}
		int num = memberService.updatePassword(member.getId(), AppMD5Utils.getMD5(newPassword));
		
		if(num>0){
			return RsobjectResult.success();
		}
		return RsobjectResult.define(StatusConstants.MEMBER_UPDATE_PASSWORD_IS_ERROR.getCode(), StatusConstants.MEMBER_UPDATE_PASSWORD_IS_ERROR.getMsg());
		
	}
	
	//重置密码
	@RequestMapping("/updatePassword")
	public RsobjectResult<?> updatePassword(String userName,String oldPassword,String newPassword){
		if(StringUtils.isEmpty(userName)&& StringUtils.isEmpty(newPassword)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		
		Member member =  memberService.getMember(userName, oldPassword);
		if(member==null){
			return RsobjectResult.define(StatusConstants.MEMBER_OLD_PASSWORD_IS_ERROR.getCode(), StatusConstants.MEMBER_OLD_PASSWORD_IS_ERROR.getMsg());
		}
		int num = memberService.updatePassword(member.getId(), AppMD5Utils.getMD5(newPassword));
		if(num>0){
			return RsobjectResult.success();
		}
		return RsobjectResult.define(StatusConstants.MEMBER_UPDATE_PASSWORD_IS_ERROR.getCode(), StatusConstants.MEMBER_UPDATE_PASSWORD_IS_ERROR.getMsg());
		
	}
	
	@RequestMapping("/bindPhoneNo")
	public RsobjectResult<?> bindPhoneNo(Long id ,String phoneNo){
		Member member = memberService.getMemberById(id);
		
		if(!StringUtils.isEmpty(member.getPhoneNo())){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_BIND_PHONE.getCode(), StatusConstants.MEMBER_USER_IS_BIND_PHONE.getMsg());
		}
		
		Member m = memberService.getMemberByPhone(phoneNo);
		
		if(m!=null){
			return RsobjectResult.define(StatusConstants.MEMBER_PHONE_NUM_IS_EXIST.getCode(), StatusConstants.MEMBER_PHONE_NUM_IS_EXIST.getMsg());
		}
		int num = memberService.updatePhoneNo(id, phoneNo);
		
		if(num>0){
			return RsobjectResult.success();
		}
		return RsobjectResult.define(StatusConstants.MEMBER_UPDATE_PHONE_NUM_IS_ERROR.getCode(), StatusConstants.MEMBER_UPDATE_PHONE_NUM_IS_ERROR.getMsg());
	}
	
	
	//实名认证
	@RequestMapping("/realName")
	public RsobjectResult<?> realName(HttpServletResponse response,Member member){
		response.setHeader("Access-Control-Allow-Origin", "*");
		checkVipLevel(member);
		if(StringUtils.isEmpty(member)){
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
		int num = memberService.realName(member);
		
		if(num>0){
			member = checkVipLevel(member);
			memberService.updateMemberVip(member);
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member);
		}
		
		return RsobjectResult.define(StatusConstants.MEMBER_USER_REGIST_IS_ERROR.getCode(), StatusConstants.MEMBER_USER_REGIST_IS_ERROR.getMsg());
		
	}
	
	//检查淘气值等级
	public static Member checkVipLevel(Member member)
	{
		Integer level = Integer.parseInt(getLevel(member.getWwAccount())) ;
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
			if(level>=0&&level<10)
			{
				member.setTbVip(1);
			}else if(level>=10&&level<20)
			{
				member.setTbVip(2);
			}else if(level >=20)
			{
				member.setTbVip(3);
			}
			member.setTbVipLevel(0);
		}
		return member;
	}
	
	 public static int differentDays(Date date1,Date date2)
	    {
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	       int day1= cal1.get(Calendar.DAY_OF_YEAR);
	        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	        
	        int year1 = cal1.get(Calendar.YEAR);
	        int year2 = cal2.get(Calendar.YEAR);
	        if(year1 != year2)   //同一年
	        {
	            int timeDistance = 0 ;
	            for(int i = year1 ; i < year2 ; i ++)
	            {
	                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	                {
	                    timeDistance += 366;
	                }
	                else    //不是闰年
	                {
	                    timeDistance += 365;
	                }
	            }
	            
	            return timeDistance + (day2-day1) ;
	        }
	        else    //不同年
	        {
	            System.out.println("判断day2 - day1 : " + (day2-day1));
	            return day2-day1;
	        }
	    }
	
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
	
	public static String checkTbAccount(String account,int status){
		
		HashMap<String, String> paramsMap = new HashMap<>();
		
		paramsMap.put("account", account);
		if(status==1)
		{
			paramsMap.put("type", "2");
		}
		
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
	
	@RequestMapping("/checkUserOrder")
	public RsobjectResult<?> checkUserOrder(Long userId,Long shopId){
		Member member = memberService.getMemberById(userId);
		System.out.println("-----------"+member);
		if(member==null){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NULL.getCode(), StatusConstants.MEMBER_USER_IS_NULL.getMsg());
		}
		if(member.getTrueNum()<=0){
			return RsobjectResult.define(StatusConstants.MEMBER_TRUE_NUM_IS_NULL.getCode(), StatusConstants.MEMBER_TRUE_NUM_IS_NULL.getMsg());
		}
		List<Order> list = orderService.getByuerOrder(userId);
		
		if(list.size()>0){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_ORDER_IS_RUNNING.getCode(), StatusConstants.MEMBER_USER_ORDER_IS_RUNNING.getMsg());
		}
		
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), null);
	}
	
	@RequestMapping("/getMemberTrueNum")
	public RsobjectResult<?> getMemberTrueNum(Long userId){
		Member member = memberService.getMemberById(userId);
		if(member==null){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NULL.getCode(), StatusConstants.MEMBER_USER_IS_NULL.getMsg());
		}
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member.getTrueNum());
	}
	
	
	//体现
	@RequestMapping("/getCash")
	public RsobjectResult<?> getCash(Long userId,BigDecimal cashAmount){
		Member member = memberService.getMemberById(userId);
		if(member==null){
			return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NULL.getCode(), StatusConstants.MEMBER_USER_IS_NULL.getMsg());
		}
		if(member.getCashbackAmount().compareTo(cashAmount)==-1)
		{
			RsobjectResult.define(StatusConstants.MEMBER_CASHAMOUNT_LESS.getCode(),StatusConstants.MEMBER_CASHAMOUNT_LESS.getMsg());
		}
		member = memberService.getCash(userId, cashAmount);
		return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member);
	}
	
	
		@RequestMapping("/getCashAmount")
		public RsobjectResult<?> getCashAmount(Long id){
			if(id==null){
				return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NULL.getCode(), StatusConstants.MEMBER_USER_IS_NULL.getMsg());
			}
			Member member = memberService.getMemberById(id);
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), member.getCashbackAmount());
		}
		
		@RequestMapping("/getCashList")
		public RsobjectResult<?> getCashList(Long id,Integer pageSize,Integer pageCount){
			if(id==null){
				return RsobjectResult.define(StatusConstants.MEMBER_USER_IS_NULL.getCode(), StatusConstants.MEMBER_USER_IS_NULL.getMsg());
			}
			List<CashBack> lstCash = cashService.getCashList(id,pageSize, pageCount);
			int count = cashService.getCashCount(id);
			int total = count/pageCount+(count%pageCount>0?1:0);
			return RsobjectResult.defines(StatusConstants.SUSSESS.getCode(), lstCash,String.valueOf(total));
		}
	

}
