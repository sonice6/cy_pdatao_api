package com.pdatao.api.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


/**
 * Created by Administrator on 2017/11/26.
 */

public class HttpUtils {
	public static String sendRequest(String urlStr, HashMap<String, String> headerMap, String host, String port) {
		return sendRequest(urlStr, headerMap, null, host, port);
	}

	public static String sendRequest(String urlStr, HashMap<String, String> headerMap) {
		return sendRequest(urlStr, headerMap, null, "", "");
	}

	public static String sendRequest(String urlStr) {
		return sendRequest(urlStr, null, null, "", "");
	}

	public static String sendRequest(String urlStr, Map<String, String> headerMap,
			Map<String, String> postHashMap, String host, String port) {
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

			httpURLConnection.setRequestMethod(postHashMap != null&&postHashMap.size()>0 ? "POST" : "GET");

			httpURLConnection.addRequestProperty("Host", "acs.m.taobao.com");
			httpURLConnection.addRequestProperty("Connection", "close");
			if (headerMap != null) {
				for (Map.Entry entry : headerMap.entrySet()) {
					httpURLConnection.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
				}
				if (!headerMap.containsKey("Accept-Encoding")) {
					httpURLConnection.addRequestProperty("Accept-Encoding", "gzip");
				}
			}
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setInstanceFollowRedirects(false);
			httpURLConnection.connect();

			if (postHashMap != null&&postHashMap.size()>0) {
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
			

			String type=getContentEncoding(httpURLConnection.getContentType());
			//System.out.println("content type:"+type);
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
				if(TextUtils.isEmpty(type)){
					resultData = out.toString();
				}else {
					resultData = out.toString(type);
				}
				out.close();

				gzip.close();
			} else {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader bufferReader = new BufferedReader(isr);
				String inputLine = "";

				while ((inputLine = bufferReader.readLine()) != null) {
					resultData += inputLine + "\n";
				}

				isr.close();
				bufferReader.close();
			}
			is.close();

			System.out.println(resultData);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	private static String getContentEncoding(String contentType) {
		String reg="charset=(.+)";
		Pattern r = Pattern.compile(reg);
		Matcher m = r.matcher(contentType);
		String charset="";
		 while (m.find()) {
			 charset= m.group(1);
		 }
		 return charset;
	}

}
