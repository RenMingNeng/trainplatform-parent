package com.bossien.train.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author mo.xf
 */
public class MD5Utils {

    private static final String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String encode(String signData) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = md5.digest(signData.getBytes("utf-8"));
            String passwordMD5 = byteArrayToHexString(byteArray);
            return passwordMD5;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return signData;
    }

    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        for (byte b : byteArray) {
            sb.append(byteToHexChar(b));
        }
        return sb.toString();
    }

    private static Object byteToHexChar(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hex[d1] + hex[d2];
    }

    /**
     * MD5加密(32位)
     *
     * @param instr 要加密的字符串
     * @return 返回加密后的字符串
     */
    public final static String encoderByMd5With32Bit(String instr) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            if (instr != null && !"".equals(instr)) {
                byte[] strTemp = instr.getBytes();
                // MD5计算方法
                MessageDigest mdTemp = MessageDigest.getInstance("MD5");
                mdTemp.update(strTemp);
                byte[] md = mdTemp.digest();
                int j = md.length;
                char str[] = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5转换
     *
     * @param plainText
     * @return MD5字符串
     */
    public static String getMD5(String plainText) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 error:", e);
        }
        messageDigest.update(plainText.getBytes());
        byte[] by = messageDigest.digest();

        StringBuffer buf = new StringBuffer();
        int val;
        for (int i = 0; i < by.length; i++) {
            val = by[i];
            if (val < 0) {
                val += 256;
            } else if (val < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(val));
        }
        return buf.toString().toUpperCase();
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @return
     */
    public static String getFileMD5(InputStream in) {
        MessageDigest digest = null;
        byte[] buffer = new byte[2048];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                System.out.println("关闭输入输出流异常 " + ex);
            }
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        String result = bigInt.toString(16);
        int length = result.length();
        if (length < 32) {
            for (int i = 0; i < (32 - length); i++) {
                result = "0" + result;
            }
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //System.out.println(MD5Utils.encoderByMd5With32Bit("123456eyblog")) ;
        File file = new File("D://upload/AG17214A003_d_001.png");
        FileInputStream in = new FileInputStream(file);
        System.out.println(getFileMD5(in));
    }

}
