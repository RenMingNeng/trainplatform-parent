package com.bossien.train.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明:签名验签工具类
 * @author xfgeng
 * @date 2017-11-28
 */
public class SignUtils {
    private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 获取待签名计算参数拼接串
     * @param paramMap 签名参数
     * @param secretKey 签名密钥
     * @return 待签名计算字符串
     */
    public static String getSignData(Map<String , String> paramMap,String secretKey){
    	SortedMap<String, String> smap = new TreeMap<String, String>(paramMap);
    	smap.remove("signValue");
    	smap.put("secretKey",secretKey);
        String signData = null;
        try {
        	signData = JsonUtils.toString(smap);
            logger.info("[signData]"+signData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signData;
    }
    
    /**
     * 获取签名值
     * @param signData  签名参数
     * @return signValue 签名值
     */
    public static String getSignValue(String signData){
        String signValue = MD5Utils.encode(signData);
        return signValue;
    }
    
    /**
     * 获取签名值
     * @param paramMap  签名参数
     * @param secretKey 签名密钥
     * @return
     */
    public static String getSignValue(Map<String,String> paramMap,String secretKey){
	     String signData = getSignData(paramMap,secretKey);
	     String signValue = MD5Utils.encode(signData);
	     return signValue;
    }

    /**
     * 验证签名
     * @param signData 待签名计算参数
     * @param signValue 待验证签名值
     * @return boolean 验签是否通过
     */
    public static boolean verifySign(String signData, String signValue){
        if(signValue.equals(getSignValue(signData))){
            return true;
        }else{
            return false;
        }
    }
    

    /**
     * 验证签名
     * @param paramMap 验证签名参数
     * @param secretKey 签名私钥
     * @return boolean 验签是否通过
     */
    public static boolean verifySign(Map<String,String> paramMap, String secretKey){
        if (StringUtils.isEmpty(secretKey)){
            logger.info("secretKey is null");
            return false;
        }
        String signValue = paramMap.get("signValue");
        if (StringUtils.isEmpty(signValue)){
            logger.info("signValue is null");
            return false;
        }
        String sign = getSignValue(paramMap, secretKey);
        if(StringUtils.isEmpty(sign)){
            logger.info("sign is null");
            return false;
        }
        logger.info("[signValue]"+signValue+",[sign]"+sign+",[secretKey]"+secretKey);
        if(signValue.equals(sign)){
            return true;
        }else{
            return false;
        }
    }
    
    public static void main(String[] args) {
        Map<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("version","1.0.0");
        paramMap.put("appCode","000");
        paramMap.put("returnUrl","http://10.36.0.81:8081/SSOAClient/index.htm");
        paramMap.put("signValue","bdf2c818ea7d36ceedfd63472c6fd840");
		System.out.println(verifySign(paramMap,"123456789"));
    }

}
