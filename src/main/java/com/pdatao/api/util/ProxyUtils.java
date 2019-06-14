package com.pdatao.api.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProxyUtils {



    private static final String BASE_URL="http://174.139.156.217:5001";
    private static final String PROXY_PZJ_URL=BASE_URL + "/pzj";
    private static final String PROXY_PZ_URL=BASE_URL + "/pz";
    private static final String PROXY_P_URL=BASE_URL + "/p";

//    private final String PROXY_URL="http://127.0.0.1:5001/pzj";
    public static JSONObject getProxyOfCity(String province,String city){
        HashMap<String, String> postParams = new HashMap<>();
        // TODO: 特殊省市的代理，比如自治区之类的
        if(!TextUtils.isEmpty(province)){
            province = province.replace("省", "");
        }
        if(!TextUtils.isEmpty(city)){
            city = city.replace("省", "市");
        }

        postParams.put("province", province);
        postParams.put("city", city);

        for(int i = 0;i<3;i++){
            try {
                String response = ApiClient
                        .sendRequest(
                                PROXY_PZJ_URL,
                                postParams,null);
                if(!TextUtils.isEmpty(response)){
                    JSONObject j = new  JSONObject(response);
                    j.put("touched_at", System.currentTimeMillis()/1000);
//                    System.out.println(j);
                    return j;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String[] refreshProxy() {
        //--------------------------获取代理ip开始-------------------------------
//        OkHttpClient client=new OkHttpClient();
//        Request proxyRequest=new Request.Builder()
//                .url("http://127.0.0.1:5001/p").build();
//        String s = client.newCall(proxyRequest).execute().body().string();
//        String ipAndPort = s.trim().replace("http://", "").replace("\n","").replace("https://", "");
//        String[] ipPorts = ipAndPort.split(":");
//        System.out.println(ipAndPort);
        String s = TextUtils.restTemplate.getForObject(PROXY_P_URL, String.class);
        String[] ipPorts = s.trim().replace("http://", "").replace("\n","").replace("https://", "").split(":");
//        System.out.println(ipPorts);
        return ipPorts;
        //--------------------------获取代理ip结束-------------------------------
    }

    public static String[] refreshProxy(String province, String city) {
        //--------------------------获取代理ip开始-------------------------------
//        OkHttpClient client=new OkHttpClient();
//        Request proxyRequest=new Request.Builder()
//                .url("http://127.0.0.1:5001/p").build();
//        String s = client.newCall(proxyRequest).execute().body().string();
//        String ipAndPort = s.trim().replace("http://", "").replace("\n","").replace("https://", "");
//        String[] ipPorts = ipAndPort.split(":");
//        System.out.println(ipAndPort);
        String s = TextUtils.restTemplate.getForObject(PROXY_PZ_URL+"?province="+ province +"&city="+ city, String.class);
        String[] ipPorts = s.trim().replace("http://", "").replace("\n","").replace("https://", "").split(":");
//        System.out.println(ipPorts);
        return ipPorts;
        //--------------------------获取代理ip结束-------------------------------
    }
}
