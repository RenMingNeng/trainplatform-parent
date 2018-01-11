package com.bossien.train.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public final class HttpClientUtils {
	
	public static String doGet(String url) {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		try {
			//设置请求报文头的编码
			getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			//设置期望返回的报文头编码
			getMethod.setRequestHeader("Accept", "text/plain;charset=utf-8");
			httpClient.executeMethod(getMethod);
			String userJson = getMethod.getResponseBodyAsString();
			getMethod.releaseConnection();
			return userJson;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doPostByJson(String url, String json) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		try {
			//设置请求报文头的编码
			postMethod.setRequestHeader("Content-Type", "application/json");
			RequestEntity requestEntity = new StringRequestEntity(json, "text/xml", "UTF-8");
			postMethod.setRequestEntity(requestEntity);

			//设置期望返回的报文头编码
			httpClient.executeMethod(postMethod);
			String userJson = postMethod.getResponseBodyAsString();
			postMethod.releaseConnection();
			return userJson;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String doPost(String url,Map<String,String> params) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		for (Entry<String,String> entry : params.entrySet()) {
			postMethod.addParameter(entry.getKey(),entry.getValue());
		}
		try {
			httpClient.executeMethod(postMethod);
			String userJson = postMethod.getResponseBodyAsString();
			postMethod.releaseConnection();
			return userJson;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		//String url = "http://www.sso.com:8080/app-uc-manage/uc/getUserInfoByOpenId.do";
		//String url = "http://10.36.1.242:28080/app-uc-manage/uc/getUserInfoByOpenId.do";
		//String url = "http://www.sso.com:8080/app-uc-manage/uc/login.do";
		String url = "http://10.36.1.242:28080/app-uc-manage/uc/login.do?appCode=000&account=13818425722&password=e10adc3949ba59abbe56e057f20f883e";
		/*Map<String,String> params = new HashMap<String,String>();
		params.put("mobileNo", "13818425721");*/
		//params.put("openId", "3e153ccd2047d6bd00b176ef41da5160");
		String result = doGet(url);
		System.out.println(result);

		/*Message message = JSONUtils.toObject(result, Message.class);
		if("000000".equals(message.getCode())){
			//解密用户数据
			String userJson = AESUtils.decrypt(String.valueOf(message.getData()), AppConstants.APP_KEY);
			System.out.println(userJson);
			UserInfoVo userInfoVo = JSONUtils.toObject(userJson, UserInfoVo.class);
			System.out.println(userInfoVo.getVarOpenId());
			System.out.println(userInfoVo.getVarMobileNo());
		}*/

	}
	
}
