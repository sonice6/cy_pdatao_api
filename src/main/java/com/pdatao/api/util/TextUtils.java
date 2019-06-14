package com.pdatao.api.util;


import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.springframework.web.client.RestTemplate;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class TextUtils {
	
public static RestTemplate restTemplate;
    

	static{
	    restTemplate = new RestTemplate();
    }
	
	public static boolean isEmpty( CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String createInstallId() {
        return UUID.randomUUID().toString().replaceAll("-", "");

        //return radomString(32);
    }
    public static String createUUID(){
        return radomNum(15)+"-"+radomString(12);
    }

    public static String createAndroidId() {
        return radomString(16);
    }
    public static String createCartuuid(){
        return "00000000000000"+UUID.randomUUID();
    }
    public static String createwhwswswws() {
        return radomStringUp(58);
    }
    private static String radomNum(int count) {
        return radomWords("1234567890",16);
    }

    private static String radomString(int count) {
        return radomWords("abcdefghijklmnopqrstuvwxyz1234567890",16);
    }
    private static String radomStringUp(int count) {
        return radomWords("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890",count);
    }


    private static  String radomWords(String base,int count)
    {
        //String base = "abcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String realDeviceId=sb.toString();
        //System.out.println(mDeviceId);
        return realDeviceId;
    }

    public static String[] refreshProxy() throws IOException {
        //--------------------------获取代理ip开始-------------------------------
        OkHttpClient client=new OkHttpClient();
        Request proxyRequest=new Request.Builder()
                .url("http://174.139.156.217:5001/p").build();
        String s = client.newCall(proxyRequest).execute().body().string();
        String ipAndPort = s.trim().replace("http://", "").replace("\n","").replace("https://", "");
        String[] ipPorts = ipAndPort.split(":");
        System.out.println(ipAndPort);
        return ipPorts;
        //--------------------------获取代理ip结束-------------------------------
    }

}
