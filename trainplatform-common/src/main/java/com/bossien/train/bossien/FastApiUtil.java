//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bossien.train.bossien;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class FastApiUtil {
    public FastApiUtil() {
    }

    public static FastResInfo initBeforeUpload(String storageServerHostPortContext, String user, String fileName, boolean isEncrypt) {
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(fileName)) {
            try {
                fileName = URLEncoder.encode(fileName, "utf-8");
            } catch (UnsupportedEncodingException var20) {
                var20.printStackTrace();
            }

            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            String sign = MD5Utils.toMD5("8a5ec6ff1abd4bac9298ccff512190df" + timeStamp).toLowerCase();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("userKey=");
            stringBuffer.append(user);
            stringBuffer.append("&timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&sign=");
            stringBuffer.append(sign);
            stringBuffer.append("&fileName=");
            stringBuffer.append(fileName);
            stringBuffer.append("&fileType=");
            stringBuffer.append(suffix);
            if (isEncrypt) {
                stringBuffer.append("&crypto=1");
            } else {
                stringBuffer.append("&crypto=0");
            }

            stringBuffer.append("&token=");
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = HttpUtil.sendGet(storageServerHostPortContext + "/api/v1/file/init", stringBuffer.toString());
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
            String status = String.valueOf(jsonObject.get("status"));
            String msg = String.valueOf(jsonObject.get("msg"));
            FastResInfo fastResInfo = new FastResInfo(status, msg);
            if ("200".equals(status)) {
                String result = String.valueOf(jsonObject.get("result"));
                JsonObject resultObject = jsonParser.parse(result).getAsJsonObject();
                JsonElement fid = resultObject.get("fid");
                if (null != fid) {
                    fastResInfo.setFid(fid.toString().trim().replace("\"", ""));
                }

                JsonElement url = resultObject.get("url");
                if (null != url) {
                    fastResInfo.setUrl(url.toString().trim().replace("\"", ""));
                }

                JsonElement token = resultObject.get("token");
                if (null != token) {
                    fastResInfo.setToken(token.toString().trim().replace("\"", ""));
                }

                JsonElement offset = resultObject.get("offset");
                if (null != offset) {
                    fastResInfo.setOffset(offset.toString().trim().replace("\"", ""));
                }
            }

            return fastResInfo;
        } else {
            return new FastResInfo("", "userKey �� fileName Ϊ��!");
        }
    }

    public static FastResInfo upload(String storageServerHostPortContext, File file, String token, String offset) {
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(offset)) {
            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&fileSize=");
            stringBuffer.append(file.length());
            stringBuffer.append("&offset=");
            stringBuffer.append(offset);
            stringBuffer.append("&token=");
            stringBuffer.append(token);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = null;

            try {
                res = HttpUtil.postFile(file, storageServerHostPortContext + "/api/v1/file/upload", stringBuffer.toString());
            } catch (IOException var16) {
                var16.printStackTrace();
            }

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
            String status = jsonObject.get("status").toString();
            String msg = jsonObject.get("msg").toString();
            FastResInfo fastResInfo = new FastResInfo(status, msg);
            if ("200".equals(status)) {
                String result = String.valueOf(jsonObject.get("result"));
                JsonObject resultObject = jsonParser.parse(result).getAsJsonObject();
                JsonElement fid = resultObject.get("fid");
                if (null != fid) {
                    fastResInfo.setFid(fid.toString().trim().replace("\"", ""));
                }

                JsonElement url = resultObject.get("url");
                if (null != url) {
                    fastResInfo.setUrl(url.toString().trim().replace("\"", ""));
                }
            }

            return fastResInfo;
        } else {
            return new FastResInfo("", "token �� offset Ϊ��!");
        }
    }

    public static FastResInfo upload(String storageServerHostPortContext, InputStream inputStream, String token, String offset, String fileName) {
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(offset)) {
            try {
                fileName = URLEncoder.encode(fileName, "utf-8");
            } catch (UnsupportedEncodingException var18) {
                var18.printStackTrace();
            }

            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&fileSize=");
            stringBuffer.append(0);
            stringBuffer.append("&offset=");
            stringBuffer.append(offset);
            stringBuffer.append("&token=");
            stringBuffer.append(token);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = null;

            try {
                res = HttpUtil.postFile(inputStream, storageServerHostPortContext + "/api/v1/file/upload", stringBuffer.toString(), fileName);
            } catch (IOException var17) {
                var17.printStackTrace();
            }

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
            String status = jsonObject.get("status").toString();
            String msg = jsonObject.get("msg").toString();
            FastResInfo fastResInfo = new FastResInfo(status, msg);
            if ("200".equals(status)) {
                String result = String.valueOf(jsonObject.get("result"));
                JsonObject resultObject = jsonParser.parse(result).getAsJsonObject();
                JsonElement fid = resultObject.get("fid");
                if (null != fid) {
                    fastResInfo.setFid(fid.toString().trim().replace("\"", ""));
                }

                JsonElement url = resultObject.get("url");
                if (null != url) {
                    fastResInfo.setUrl(url.toString().trim().replace("\"", ""));
                }
            }

            return fastResInfo;
        } else {
            return new FastResInfo("", "token �� offset Ϊ��!");
        }
    }

    public static FastResInfo getFileInfo(String storageServerHostPortContext, String userKey, String fid) {
        if (!StringUtils.isEmpty(userKey) && !StringUtils.isEmpty(fid)) {
            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            String sign = MD5Utils.toMD5("8a5ec6ff1abd4bac9298ccff512190df" + timeStamp).toLowerCase();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("userKey=");
            stringBuffer.append(userKey);
            stringBuffer.append("&timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&sign=");
            stringBuffer.append(sign);
            stringBuffer.append("&fid=");
            stringBuffer.append(fid);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = HttpUtil.sendGet(storageServerHostPortContext + "/api/v1/file/find", stringBuffer.toString());
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
            String status = jsonObject.get("status").toString();
            String msg = jsonObject.get("msg").toString();
            FastResInfo fastResInfo = new FastResInfo(status, msg);
            if ("200".equals(status)) {
                String result = String.valueOf(jsonObject.get("result"));
                JsonObject resultObject = jsonParser.parse(result).getAsJsonObject();
                JsonElement jfid = resultObject.get("fid");
                if (null != jfid) {
                    fastResInfo.setFid(jfid.toString().trim().replace("\"", ""));
                }

                JsonElement filename = resultObject.get("fileName");
                if (null != filename) {
                    fastResInfo.setFileName(filename.toString().trim().replace("\"", ""));
                }

                JsonElement filesize = resultObject.get("fileSize");
                if (null != filesize) {
                    fastResInfo.setFileSize(filesize.toString().trim().replace("\"", ""));
                }

                JsonElement createtime = resultObject.get("createTime");
                if (null != createtime) {
                    fastResInfo.setCreateTime(createtime.toString().trim().replace("\"", ""));
                }

                JsonElement url = resultObject.get("url");
                if (null != url) {
                    fastResInfo.setUrl(url.toString().trim().replace("\"", ""));
                }
            }

            return fastResInfo;
        } else {
            return new FastResInfo("", "userKey �� fid Ϊ��!");
        }
    }

    public static List<FastResInfo> getGroupFileInfo(String storageServerHostPortContext, String userKey) {
        if (StringUtils.isEmpty(userKey)) {
            return null;
        } else {
            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            String sign = MD5Utils.toMD5("8a5ec6ff1abd4bac9298ccff512190df" + timeStamp).toLowerCase();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("userKey=");
            stringBuffer.append(userKey);
            stringBuffer.append("&timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&sign=");
            stringBuffer.append(sign);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = HttpUtil.sendGet(storageServerHostPortContext + "/api/v1/file/group/find", stringBuffer.toString());
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
            String status = jsonObject.get("status").toString();
            String msg = jsonObject.get("msg").toString();
            new FastResInfo(status, msg);
            List<FastResInfo> list = new ArrayList();
            if ("200".equals(status)) {
                String result = String.valueOf(jsonObject.get("result"));
                JsonArray resultArray = jsonParser.parse(result).getAsJsonArray();

                FastResInfo fastResInfo;
                for(Iterator var14 = resultArray.iterator(); var14.hasNext(); list.add(fastResInfo)) {
                    JsonElement jsonElement = (JsonElement)var14.next();
                    fastResInfo = new FastResInfo(status, msg);
                    String element = jsonElement.toString();
                    JsonObject resultObject = jsonParser.parse(element).getAsJsonObject();
                    JsonElement jfid = resultObject.get("fid");
                    if (null != jfid) {
                        fastResInfo.setFid(jfid.toString().trim().replace("\"", ""));
                    }

                    JsonElement filename = resultObject.get("fileName");
                    if (null != filename) {
                        fastResInfo.setFileName(filename.toString().trim().replace("\"", ""));
                    }

                    JsonElement filesize = resultObject.get("fileSize");
                    if (null != filesize) {
                        fastResInfo.setFileSize(filesize.toString().trim().replace("\"", ""));
                    }

                    JsonElement createtime = resultObject.get("createTime");
                    if (null != createtime) {
                        fastResInfo.setCreateTime(createtime.toString().trim().replace("\"", ""));
                    }

                    JsonElement url = resultObject.get("url");
                    if (null != url) {
                        fastResInfo.setUrl(url.toString().trim().replace("\"", ""));
                    }

                    JsonElement token = resultObject.get("token");
                    if (null != token) {
                        fastResInfo.setToken(token.toString().trim().replace("\"", ""));
                    }
                }
            }

            return list;
        }
    }

    public static FastResInfo downLoad(String storageServerHostPortContext, String user, String key, String fid, String dir, String fileName) {
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(fid)) {
            try {
                fileName = URLEncoder.encode(fileName, "utf-8");
            } catch (UnsupportedEncodingException var16) {
                var16.printStackTrace();
            }

            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            String sign = MD5Utils.toMD5(key + timeStamp).toLowerCase();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("userKey=");
            stringBuffer.append(user);
            stringBuffer.append("&timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&sign=");
            stringBuffer.append(sign);
            File destFile = new File(dir + File.separator + fileName);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = HttpUtil.getFile(storageServerHostPortContext + "/api/v1/file/download/" + fid, stringBuffer.toString(), destFile);
            FastResInfo fastResInfo = null;
            if (StringUtils.isNotEmpty(res)) {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
                String status = jsonObject.get("status").toString();
                String msg = jsonObject.get("msg").toString();
                fastResInfo = new FastResInfo(status, msg);
            } else {
                fastResInfo = new FastResInfo("200", "ok");
            }

            return fastResInfo;
        } else {
            return new FastResInfo("", "userKey �� fid Ϊ��!");
        }
    }

    public static FastResInfo downLoad(String storageServerHostPortContext, String user, String key, String fid, File destFile) {
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(fid)) {
            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            String sign = MD5Utils.toMD5(key + timeStamp).toLowerCase();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("userKey=");
            stringBuffer.append(user);
            stringBuffer.append("&timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&sign=");
            stringBuffer.append(sign);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = HttpUtil.getFile(storageServerHostPortContext + "/api/v1/file/download/" + fid, stringBuffer.toString(), destFile);
            FastResInfo fastResInfo = null;
            if (StringUtils.isNotEmpty(res)) {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
                String status = jsonObject.get("status").toString();
                String msg = jsonObject.get("msg").toString();
                fastResInfo = new FastResInfo(status, msg);
            } else {
                fastResInfo = new FastResInfo("200", "ok");
            }

            return fastResInfo;
        } else {
            return new FastResInfo("", "userKey �� fid Ϊ��!");
        }
    }

    public static FastResInfo downLoad(String storageServerHostPortContext, String userKey, String key, String fid, OutputStream out) {
        if (!StringUtils.isEmpty(userKey) && !StringUtils.isEmpty(fid)) {
            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            String sign = MD5Utils.toMD5(key + timeStamp).toLowerCase();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("userKey=");
            stringBuffer.append(userKey);
            stringBuffer.append("&timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&sign=");
            stringBuffer.append(sign);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = HttpUtil.getFile(storageServerHostPortContext + "/api/v1/file/download/" + fid, stringBuffer.toString(), out);
            FastResInfo fastResInfo = null;
            if (StringUtils.isNotEmpty(res)) {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
                String status = jsonObject.get("status").toString();
                String msg = jsonObject.get("msg").toString();
                fastResInfo = new FastResInfo(status, msg);
            } else {
                fastResInfo = new FastResInfo("200", "ok");
            }

            return fastResInfo;
        } else {
            return new FastResInfo("", "userKey �� fid Ϊ��!");
        }
    }

    public static FastResInfo delete(String storageServerHostPortContext, String userKey, String key, String fid) {
        if (!StringUtils.isEmpty(userKey) && !StringUtils.isEmpty(fid)) {
            String timeStamp = System.currentTimeMillis() / 1000L + Constant.EXPIRES_TIME.longValue() + "";
            String sign = MD5Utils.toMD5(key + timeStamp).toLowerCase();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("userKey=");
            stringBuffer.append(userKey);
            stringBuffer.append("&timeStamp=");
            stringBuffer.append(timeStamp);
            stringBuffer.append("&sign=");
            stringBuffer.append(sign);
            stringBuffer.append("&fid=");
            stringBuffer.append(fid);
            if (StringUtils.isEmpty(storageServerHostPortContext)) {
                storageServerHostPortContext = "http://10.36.1.243:28084/storage/";
            }

            String res = HttpUtil.sendGet(storageServerHostPortContext + "/api/v1/file/delete", stringBuffer.toString());
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(res).getAsJsonObject();
            String status = jsonObject.get("status").toString();
            String msg = jsonObject.get("msg").toString();
            FastResInfo fastResInfo = new FastResInfo(status, msg);
            return fastResInfo;
        } else {
            return new FastResInfo("", "userKey �� fid Ϊ��!");
        }
    }

    public static void main(String[] args) {
        File file = new File("D:" + File.separator + "test/test_1.mp4");
        FastResInfo fastResInfo1 = initBeforeUpload("http://10.36.1.243:28084/storage/", "user", "test_1.mp4", false);
        FastResInfo fastResInfo2 = upload("http://10.36.1.243:28084/storage/", file, fastResInfo1.getToken(), "0");
        System.out.println(fastResInfo2.getStatus());
        System.out.println(fastResInfo2.getUrl());
        FastResInfo fastResInfo = downLoad("http://10.36.1.243:28084/storage/", "user", "8a5ec6ff1abd4bac9298ccff512190df", "81285ce6dfdd4087b95d0db467082352.mp4", "d:\\", "����.ts");
    }
}
