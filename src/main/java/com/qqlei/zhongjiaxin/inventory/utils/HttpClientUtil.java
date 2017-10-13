package com.qqlei.zhongjiaxin.inventory.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
	private static final Log Logger = LogFactory.getLog(HttpClientUtil.class);

	public static ThreadLocal<HttpClientContext> LOCAL = new ThreadLocal<HttpClientContext>();

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";


	/**
	 * 默认连接超时时间
	 */
	private  static int DEFAULT_CONNECTION_TIME_OUT = 10000;
	/**
	 *
	 */
	private  static int DEFAULT_SOCKET_TIME_OUT = 60000;

	/***
	 * 默认字符编码
	 */
	private final static String DEFAULT_CHARSET_UTF_8 = "UTF-8";

	public static HttpClient getHttpClient(){
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// 连接池最大生成连接数200
		cm.setMaxTotal(200);
		// 默认设置route最大连接数为20
		cm.setDefaultMaxPerRoute(20);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIME_OUT)
				.setConnectTimeout(DEFAULT_CONNECTION_TIME_OUT).build();// 设置请求和传输超时时间
		HttpClientBuilder builder = HttpClients.custom();
		HttpClient httpClient =  builder.setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
		return httpClient;
	}

	public static String post(String param, String url) {
		String res = "";
		HttpResponse response ;
		HttpPost post = null;
		HttpClient httpClient = getHttpClient();
		try {
			HttpContext context = LOCAL.get();
			post = new HttpPost(url);
			post.addHeader(HTTP.CONTENT_TYPE,APPLICATION_JSON);
			StringEntity se = new StringEntity(param,DEFAULT_CHARSET_UTF_8);//设置编码
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			se.setContentEncoding(DEFAULT_CHARSET_UTF_8);
			post.setEntity(se);
			response = httpClient.execute(post, context);
			Logger.debug("Headers-->" + Arrays.toString(response.getAllHeaders()));
			res = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET_UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
		return res;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * @param url
	 *            发送请求的URL
	 * @return URL 所代表远程资源的响应结果
	 */
//	public static String sendGet(String url) {
//		String res = "";
//		HttpResponse response ;
//		HttpGet get = null;
//		HttpClient httpClient = getHttpClient();
//		try {
//			get = new HttpGet(url);
//			response =httpClient.execute(get);
////			Logger.debug("Headers-->" + Arrays.toString(response.getAllHeaders()));
//			res = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET_UTF_8);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (get != null) {
//				get.releaseConnection();
//			}
//		}
//		return res;
//	}

	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url ;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
}
