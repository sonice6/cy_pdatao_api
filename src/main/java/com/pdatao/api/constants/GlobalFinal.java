package com.pdatao.api.constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.pdatao.api.entity.TbUser;
import com.pdatao.api.util.ProxyUtils;

public class GlobalFinal {
	
	public static final String appVersion ="7.6.0";

	public static final Map<String, String> map = new HashMap<String, String>();

	// 使用线程安全的Hashmap
	public static final Map<Integer, JSONObject> proxiesMap = new ConcurrentHashMap<>();

	public static String[] getProxyForUser(TbUser user) throws JSONException{
		JSONObject o = proxiesMap.get(user.getId());
        try {
            // 还剩30S以上则返回，否则更换IP
            if(o != null && o.getLong("end") > (System.currentTimeMillis()/1000 + 30)){
                return new String[] {o.get("ip").toString(), o.get("port").toString()};
            }else{
                throw  new IOException("无代理或将失效");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            o = ProxyUtils.getProxyOfCity(user.getProvince(), user.getCity());
            o.put("touched_at", System.currentTimeMillis()/1000);
            proxiesMap.put(user.getId(),o);
            return new String[] {o.get("ip").toString(), o.get("port").toString()};
        }
    }

	public static void setParams(Map<String, String> map,TbUser user){
		map.put("brandName", user.getManufacturer());
		map.put("model", user.getDeviceName());
		map.put("deviceId", user.getDeviceId());
		map.put("umt", user.getUmt());
		map.put("utdid", user.getUtdid());
		map.put("sid", user.getSid());
		map.put("hid", user.getHid());
		map.put("appVersion", appVersion);
//		String ips = getIps();
//		JSONObject json = new JSONObject(ips);
//		map.put("ip", json.get("ip").toString());
//		map.put("port", json.get("port").toString());
	}

}
