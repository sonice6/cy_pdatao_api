package com.pdatao.api.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.pdatao.api.dao.MemberMapper;
import com.pdatao.api.entity.CodeData;
import com.pdatao.api.entity.Member;
import com.pdatao.api.service.WeChatService;
import com.pdatao.api.util.AppMD5Utils;

@Component
public class WeChatServiceImpl implements WeChatService {
	

	@Autowired
	private MemberMapper memberDao;
	
	public Member WechatLogin(String Code,String appid,String secret)
	{
		//通过code得到token
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("******************************");
		System.out.println("请求获取token");
		url+="?appid="+appid+"&secret="+secret+"&code="+Code+"&grant_type=authorization_code";
		System.out.println("输出地址："+url);
		String r = restTemplate.getForObject(url, String.class);
		System.out.println("得到token:"+r);
		if(r.indexOf("access_token")==-1)
		{
			return null;
		}
		try {
			JSONObject ret = new JSONObject();
			System.out.println("json实例化");
			ret = new JSONObject(r);
			System.out.println(ret.get("access_token"));
			if(ret.get("access_token")!=null)
			{
				System.out.println("拉取用户信息");
				url="https://api.weixin.qq.com/sns/userinfo";
				url+="?access_token="+ret.get("access_token")+"&openid="+ret.get("openid")+"&lang=ZH_CN";
						System.out.println("输出地址："+url);
				r = restTemplate.getForObject(url,String.class);
				System.out.println("得到token:"+r);
				ret = new JSONObject(r);
				String openid = ret.getString("openid");
				//String unionid = ret.getString("unionid");
				System.out.println("通过openid找用户："+openid);
				Member member = memberDao.getMemberByOpenId(openid);
				
				if(member!=null)
				{
					System.out.println("判断unionid是否位空："+member.getWxUnionid());
					if(member.getWxUnionid()==null||member.getWxUnionid().equals(""))
					{
						member.setWxUnionid(member.getWxNickname());
						memberDao.updateMemberUnionid(member.getId(), member.getWxUnionid());
					}
					if(member.getWxNickname()!=null)
					{
						String s ="";
						try {
							s = new String(URLDecoder.decode(member.getWxNickname()).getBytes("ISO-8859-1"), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						member.setWxNickname(s);
					}
					return member;
				}else
				{
					System.out.println("找不到用户就添加");
					member =new Member();
					member.setUserName("");
					member.setPassword(AppMD5Utils.getMD5("123"));
					member.setWwAccount("");
					member.setWxAccount("");
					member.setPhoneNo("");
					member.setEmail("");
					member.setStatus(2);
					member.setTotalNum(0);
					member.setGoodNum(0);
					member.setVipInfo("0");
					member.setWarningNum(0);
					member.setOrderBs("0");
					member.setTrueNum(3);
					member.setWxid(openid);
					String nickname = ret.getString("nickname");
					//nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
					nickname = URLEncoder.encode(nickname);
					System.out.println("encode:"+nickname);
					System.out.println("decode:"+new String(URLDecoder.decode(nickname).getBytes("ISO-8859-1"), "UTF-8"));
					//System.out.println("中文乱码");
					//System.out.println("utf-8:"+new String(nickname.getBytes("ISO-8859-1"), "UTF-8"));
					//System.out.println("gbk:"+new String(nickname.getBytes("ISO-8859-1"), "GBK"));
					member.setWxNickname(nickname);
					member.setWxUnionid(nickname);
					String wximg = ret.getString("headimgurl");
					wximg = new String(wximg.getBytes("ISO-8859-1"), "UTF-8");
					member.setWxImage(wximg);
					System.out.println("开始添加用户");
					Integer index = memberDao.WeRegister(member);
					System.out.println("添加用户结果："+index);
					System.out.println("再次用openid查找用户");
					member = memberDao.getMemberByOpenId(openid);
					System.out.println("输出member");
					member.setWxNickname(new String(URLDecoder.decode(member.getWxNickname()).getBytes("ISO-8859-1"), "UTF-8"));
					member.setWxUnionid(member.getWxNickname());
					System.out.println("微信昵称："+member.getWxNickname());
					return member;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Member BindWw(Member mem,Integer status)
	{
	//是否在数据库中存在
		Member member =memberDao.getMemberById(mem.getId());
		member.setStatus(mem.getStatus());
		if(status!=3)
		{
			if(mem.getWwAccount()!=null)
			{
				member.setWwAccount(mem.getWwAccount());
				member.setUserName(mem.getWwAccount());
			}
			if(mem.getWxAccount()!=null)
			{
				member.setWxAccount(mem.getWxAccount());
			}
			if(status==1){
				
				CodeData codeData = memberDao.isExistsWw(mem.getWwAccount());
				
				/*if(codeData!=null)
				{
					member.setStatus(0);
				}else
				{
					member.setStatus(2);
				}*/
				
				member.setTbCreated(mem.getTbCreated());
				member.setGoodNum(mem.getGoodNum());
				member.setTotalNum(mem.getTotalNum());
				member.setVipInfo(mem.getVipInfo());
				member.setWarningNum(mem.getWarningNum());
				member.setTbSex(mem.getTbSex());
				member.setTbPromoted(mem.getTbPromoted());
				member.setTbVip(mem.getTbVip());
				member.setTbVipLevel(mem.getTbVipLevel());
				if(member.getWwBindnum()==null)
				{
					member.setWwBindnum(1);
				}else
				{
					member.setWwBindnum(member.getWwBindnum()+1);
				}
			}
		}else
		{
			if(mem.getWwAccount()!=null)
			{
				member.setWwAccount(mem.getWwAccount());
				member.setUserName(mem.getWwAccount());
			}
			if(mem.getWxAccount()!=null)
			{
				member.setWxAccount(mem.getWxAccount());
			}
			member.setStatus(mem.getStatus());
			member.setTbCreated(mem.getTbCreated());
			member.setGoodNum(mem.getGoodNum());
			member.setTotalNum(mem.getTotalNum());
			member.setVipInfo(mem.getVipInfo());
			member.setWarningNum(mem.getWarningNum());
			member.setTbSex(mem.getTbSex());
			member.setTbPromoted(mem.getTbPromoted());
			member.setTbVip(mem.getTbVip());
			member.setTbVipLevel(mem.getTbVipLevel());
		}
		memberDao.updateMemberWwAccount(member);
		return member;
	}

	@Override
	public List<Member> isExistsWwAccount(String wwAccount) {
		// TODO Auto-generated method stub
		return memberDao.isExistsWwAccount(wwAccount);
	}

	@Override
	public List<Member> isExistsWxAccount(String wxAccount) {
		// TODO Auto-generated method stub
		return memberDao.isExistsWxAccount(wxAccount);
	}

	@Override
	public Member WechatApp(String name,String code) {
		// TODO Auto-generated method stub
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		String appid="wx15947291a4086fe3";
		String secret = "e4ae8d4dbbf2ab321170e0da6a4d6e25";
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("******************************");
		System.out.println("请求获取token");
		url+="?appid="+appid+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
		System.out.println("输出地址："+url);
		String r = restTemplate.getForObject(url, String.class);
		System.out.println("得到token:"+r);
		if(r.contains("errcode"))
		{
			return null;
		}
		JSONObject ret = new JSONObject();
		System.out.println("json实例化");
		try {
			ret = new JSONObject(r);
			if(ret.getString("openid")!="")
			{
				//String unionid = ret.getString("unionid");
				String unionid = URLEncoder.encode(name);
				//通过unionid找对象
				System.out.println("name:"+name);
				System.out.println("unioid:"+unionid);
				Member member = memberDao.getMemberByUnionid(unionid);
				return member;
			}else
			{
				return null;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Member BindBankCard(Long id, String bankName ,String bankType,String bankAccount,String wxAccount) {
		// TODO Auto-generated method stub
		memberDao.BindBankCard(id, bankName,bankType,bankAccount,wxAccount);
		Member member = memberDao.getMemberById(id);
		return member;
	}
}
