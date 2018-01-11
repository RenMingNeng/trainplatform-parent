//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bossien.train.bossien;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public MD5Utils() {
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        } else {
            MessageDigest digest = null;
            FileInputStream in = null;
            byte[] buffer = new byte[10240];

            String result;
            label115: {
                try {
                    digest = MessageDigest.getInstance("MD5");
                    in = new FileInputStream(file);

                    int len;
                    while((len = in.read(buffer)) != -1) {
                        digest.update(buffer, 0, len);
                    }

                    in.close();
                    break label115;
                } catch (Exception var16) {
                    var16.printStackTrace();
                    result = null;
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException var15) {
                        System.out.println("�ر�����������쳣 " + var15);
                    }

                }

                return result;
            }

            BigInteger bigInt = new BigInteger(1, digest.digest());
            result = bigInt.toString(16);
            int length = result.length();
            if (length < 32) {
                for(int i = 0; i < 32 - length; ++i) {
                    result = "0" + result;
                }
            }

            return result;
        }
    }

    public static String toMD5(String plainText) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var6) {
            throw new RuntimeException("MD5 error:", var6);
        }

        messageDigest.update(plainText.getBytes());
        byte[] by = messageDigest.digest();
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < by.length; ++i) {
            int val = by[i];
            if (val < 0) {
                val += 256;
            } else if (val < 16) {
                buf.append("0");
            }

            buf.append(Integer.toHexString(val));
        }

        return buf.toString().toUpperCase();
    }

    public static String getMD5(String plainText) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var6) {
            throw new RuntimeException("MD5 error:", var6);
        }

        messageDigest.update(plainText.getBytes());
        byte[] by = messageDigest.digest();
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < by.length; ++i) {
            int val = by[i];
            if (val < 0) {
                val += 256;
            } else if (val < 16) {
                buf.append("0");
            }

            buf.append(Integer.toHexString(val));
        }

        return buf.toString().toUpperCase();
    }
}
