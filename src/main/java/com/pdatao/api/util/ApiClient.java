package com.pdatao.api.util;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Created by Administrator on 2017/11/26.
 */

public class ApiClient {


	public static String sendRequest(String urlStr, HashMap<String, String> headerMap, String host, String port) {
		return sendRequest(urlStr, headerMap, null, host, port);
	}

	public static String sendRequest(String urlStr, HashMap<String, String> headerMap) {
		return sendRequest(urlStr, headerMap, null, "", "");
	}
	public static String sendRequest(String urlStr, HashMap<String, String> headerMap,HashMap<String, String> postMap) {
		return sendRequest(urlStr, headerMap, postMap, "", "");
	}

	public static String sendRequest(String urlStr) {
		return sendRequest(urlStr, null, null, "", "");
	}

	public static String sendRequest(String urlStr, HashMap<String, String> headerMap,
			HashMap<String, String> postHashMap, String host, String port) {
		try {
			URL url = new URL(urlStr);

			HttpURLConnection httpURLConnection = null;

			if (!(TextUtils.isEmpty(host) && TextUtils.isEmpty(port))) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Integer.parseInt(port)));
				httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}

			httpURLConnection.setConnectTimeout(15000);
			httpURLConnection.setReadTimeout(15000);

			httpURLConnection.setRequestMethod(postHashMap != null ? "POST" : "GET");
			
			System.out.println("请求方式"+httpURLConnection.getRequestMethod());

			if (headerMap != null) {
				for (Map.Entry entry : headerMap.entrySet()) {
					httpURLConnection.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
				}
			}
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setInstanceFollowRedirects(false);
			httpURLConnection.connect();

			if (postHashMap != null) {
				// Post

				Set<Map.Entry<String, String>> entries = postHashMap.entrySet();

				Iterator iter = entries.iterator();

				StringBuilder sbParams = new StringBuilder();
				for (int i = 0; i < entries.size(); i++) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					String val = (String) entry.getValue();
					sbParams.append(key + "=" + URLEncoder.encode(val, "UTF-8"));
					if (i != entries.size() - 1) {
						sbParams.append("&");
					}
				}

				DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
				dos.writeBytes(sbParams.toString());
				dos.flush();
				dos.close();

			}
			if (httpURLConnection.getResponseCode() != 200) {
				return "";
			}

			InputStream is = httpURLConnection.getInputStream(); // 获取输入流，此时才真正建立链接

			String encoding = httpURLConnection.getHeaderField("Content-Encoding");

			String resultData = "";

			if (!TextUtils.isEmpty(encoding) && encoding.equals("gzip")) {
				ByteArrayOutputStream out = null;
				GZIPInputStream gzip = null;

				out = new ByteArrayOutputStream();
				gzip = new GZIPInputStream(is);
				byte[] buffer = new byte[1024 * 10];
				int n = 0;
				while ((n = gzip.read(buffer, 0, buffer.length)) > 0) {
					out.write(buffer, 0, n);
				}
				resultData = out.toString();
				out.close();

				gzip.close();
			} else {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader bufferReader = new BufferedReader(isr);
				String inputLine = "";

				while ((inputLine = bufferReader.readLine()) != null) {
					resultData += inputLine + "\n";
				}
				
				resultData=resultData.substring(0,resultData.length()-1);

				isr.close();
				bufferReader.close();
			}
			is.close();

			//System.out.println(resultData);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }  

}
