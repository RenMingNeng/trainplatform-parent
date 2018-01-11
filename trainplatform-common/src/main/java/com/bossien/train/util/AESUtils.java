package com.bossien.train.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * AES：对称加密算法工具类
 * 注意：AES加密和解密过程中，密钥长度都必须是16位
 *
 * Created by gengxiefeng on 2017/11/10.
 */
public class AESUtils {

    private static final String AES_TYPE = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     * @param str String
     * @param key String
     * @return String
     */
    public static String encrypt(String str, String key) {
        String result = null;
        try{
            //设置密钥
            SecretKeySpec securekey = new SecretKeySpec(key.getBytes(),"AES");
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance (AES_TYPE);
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            //正式执行加密操作
            byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
            //BASE64Encoder
            result = new BASE64Encoder().encode(bytes);
        }catch(Throwable e){
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 解密
     * @param str String
     * @param key String
     * @return String
     * @throws Exception
     */
    public static String decrypt(String str, String key) {
        String result = null;
            try{
            //设置密钥
            SecretKeySpec securekey = new SecretKeySpec(key.getBytes(),"AES");
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance (AES_TYPE);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            //base64Decoder
            byte[] bytes = new BASE64Decoder().decodeBuffer(str);
            //正式执行解密操作
            result = new String(cipher.doFinal(bytes), "utf-8");
        }catch(Throwable e){
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]) {
        //待加密内容
        String str = "待加密内容123456";
        //密钥
        String key = "A7D9A7F2EE24482F";

        //加密
        String result = AESUtils.encrypt(str,key);
        System.out.println("加密后："+result);

        //解密
        String decryResult = AESUtils.decrypt(result, key);
        System.out.println("解密后："+decryResult);

    }


}
