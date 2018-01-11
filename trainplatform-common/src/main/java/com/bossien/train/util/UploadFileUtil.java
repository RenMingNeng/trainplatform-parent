package com.bossien.train.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.bossien.train.common.AppConstant;
import com.bossien.train.bossien.FastApiUtil;
import com.bossien.train.bossien.FastResInfo;
import org.apache.log4j.Logger;

public class UploadFileUtil {

    private static final Logger logger = Logger.getLogger(UploadFileUtil.class);

    /**
     * 文件上传
     *
     * @param file_name
     * @param file
     * @param suffix
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String upFile(String file_name, File file, String suffix) throws UnsupportedEncodingException {
        boolean isEncrypt = false;
        if (AppConstant.VIDEO_SUFFIX.contains(suffix)) {
            isEncrypt = true;
        }
        FastResInfo initInfo = FastApiUtil.initBeforeUpload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, URLEncoder.encode(file_name, "utf-8"), isEncrypt);
        if (null == initInfo || !initInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("初始化失败");
        }
        FastResInfo upInfo = FastApiUtil.upload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, file, initInfo.getToken(), "0");
        if (null == upInfo || !upInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("上传失败");
        }
        return initInfo.getFid();
    }

    /**
     * 文件上传
     *
     * @param file_name
     * @param inputStream
     * @param suffix
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String upFile(String file_name, InputStream inputStream, String suffix) throws UnsupportedEncodingException {
        boolean isEncrypt = false;
        if (AppConstant.VIDEO_SUFFIX.contains(suffix)) {
            isEncrypt = true;
        }
        FastResInfo initInfo = FastApiUtil.initBeforeUpload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, URLEncoder.encode(file_name, "utf-8"), isEncrypt);
        if (null == initInfo || !initInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("初始化失败");
        }
        FastResInfo upInfo = FastApiUtil.upload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, inputStream, initInfo.getToken(), "0", URLEncoder.encode(file_name, "utf-8"));
        if (null == upInfo || !upInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("上传失败");
        }
        return initInfo.getFid();
    }

    /**
     * 文件上传
     *
     * @param file_name
     * @param local_file_name
     * @param suffix
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String upFile(String file_name, String local_file_name, String suffix) throws UnsupportedEncodingException {
        boolean isEncrypt = false;
        if (AppConstant.VIDEO_SUFFIX.contains(suffix)) {
            isEncrypt = true;
        }
        FastResInfo initInfo = FastApiUtil.initBeforeUpload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, URLEncoder.encode(file_name, "utf-8"), isEncrypt);
        if (null == initInfo || !initInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("初始化失败");
        }
        FastResInfo upInfo = FastApiUtil.upload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, new File(URLEncoder.encode(local_file_name, "utf-8")), initInfo.getToken(), "0");
        if (null == upInfo || !upInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("上传失败");
        }
        return initInfo.getFid();
    }

    /**
     * 根据file_new_name查找文件详情
     *
     * @param file_new_name
     * @return
     */
    public static FastResInfo getFileDBInfo(String file_new_name) {
        FastResInfo findInfo = FastApiUtil.getFileInfo(AppConstant.STORAGE_SERVER_HOST_PORT_LAN,
                AppConstant.FASTDFS_USER, file_new_name);
        if (null == findInfo || !findInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("查询失败");
        }
        return findInfo;
    }

    /**
     * 下载
     *
     * @param file_new_name
     * @param out
     */
    public static void download_file(String file_new_name, OutputStream out) {
        FastResInfo downInfo = FastApiUtil.downLoad(AppConstant.STORAGE_SERVER_HOST_PORT_LAN,
                AppConstant.FASTDFS_USER, AppConstant.FASTDFS_USERKEY, file_new_name, out);
        if (null == downInfo || !downInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("下载失败");
        }
    }

    /**
     * 下载
     *
     * @param file_new_name
     * @param file
     * @return
     */
    public static File download_file(String file_new_name, File file) {
        FastResInfo downInfo = FastApiUtil.downLoad(AppConstant.STORAGE_SERVER_HOST_PORT_LAN,
                AppConstant.FASTDFS_USER, AppConstant.FASTDFS_USERKEY, file_new_name, file);
        if (!downInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件下载失败");
        }
        return file;
    }

    /**
     * 下载
     *
     * @param file_new_name
     * @param local_filename
     */
    public static void download_file_path(String file_new_name, String local_filename) {
        FastResInfo downInfo = FastApiUtil.downLoad(AppConstant.STORAGE_SERVER_HOST_PORT_LAN,
                AppConstant.FASTDFS_USER, AppConstant.FASTDFS_USERKEY, file_new_name, new File(local_filename));
        if (!downInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件下载失败");
        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String 文件夹路径 如 c:/fqf
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath String 文件夹路径及名称 如c:/fqf
     * @return boolean
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            // System.out.println("删除文件夹操作出错");
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除
     *
     * @param file_new_name
     * @return
     */
    public static void delete_file(String file_new_name) {
        FastResInfo delInfo = FastApiUtil.delete(AppConstant.STORAGE_SERVER_HOST_PORT_LAN,
                AppConstant.FASTDFS_USER, AppConstant.FASTDFS_USERKEY, file_new_name);
        if (!delInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            logger.error("fastdfs文件删除失败:" + file_new_name);
        }
    }

    /**
     * 修改 (修改文件名对应的的文件)
     *
     * @param file_new_name
     * @param file
     */
    public static void modify_file(String file_new_name, File file) {
        FastResInfo delInfo = FastApiUtil.delete(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, AppConstant.FASTDFS_USERKEY, file_new_name);
        if (!delInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件删除失败");
        }
        FastResInfo initInfo = FastApiUtil.initBeforeUpload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, file_new_name, false);
        if (!initInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件初始化失败");
        }
        FastResInfo upInfo = FastApiUtil.upload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, file, initInfo.getToken(), "0");
        if (!upInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件上传失败");
        }
    }

    /**
     * 修改(修改文件名对应的的文件)
     *
     * @param file_new_name
     * @param local_file_path
     */
    public static void modify_file(String file_new_name, String local_file_path) {
        FastResInfo delInfo = FastApiUtil.delete(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, AppConstant.FASTDFS_USERKEY, file_new_name);
        if (!delInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件删除失败");
        }
        FastResInfo initInfo = FastApiUtil.initBeforeUpload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, file_new_name, false);
        if (!initInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件初始化失败");
        }
        FastResInfo upInfo = FastApiUtil.upload(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, new File(local_file_path), initInfo.getToken(), "0");
        if (!upInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)) {
            throw new RuntimeException("fastdfs文件上传失败");
        }
    }

    /**
     * 修改 (修改文件名)
     */
    /*public static void modify_file_name(String file_new_name, String update_name) {
        try {
			FastResInfo delInfo = FastApiUtil.updateFileName(AppConstant.STORAGE_SERVER_HOST_PORT_LAN, AppConstant.FASTDFS_USER, AppConstant.FASTDFS_USERKEY, file_new_name, URLEncoder.encode(update_name,"utf-8"));
			if(!delInfo.getStatus().equals(AppConstant.UPLOAD_RESULT_SUCCESS)){
				throw new RuntimeException("fastdfs文件删除失败");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}*/
    public static void write(InputStream in, OutputStream out) {
        try {
            // 流指针还原
            if (in.markSupported()) {
                in.reset();
            }
            byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
            }
        }
    }
}
