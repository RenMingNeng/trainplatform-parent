package com.bossien.train.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class ZipUtil {
	static final int BUFFER = 8192;

	private File zipFile;

	public ZipUtil(String pathName) {
		zipFile = new File(pathName);
	}

	/** 压缩文件 */
	public void compress(List<File> files) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32()); // 不加CRC32，一样可以生成文件。关于数据如何校验，请高手指点
			ZipOutputStream out = new ZipOutputStream(cos);
			out.setEncoding("gbk"); // 如果不加此句，压缩文件依然可以生成，只是在打开和解压的时候，会显示乱码，但是还是会解压出来
			String basedir = "";
			compressMutilFile(files, out, basedir);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/** 压缩多个文件 */
	private void compressMutilFile(List<File> files, ZipOutputStream out, String basedir) {
		if (null==files || files.isEmpty()) {
            return;
        }
		File file = null;
		for (int i = 0; i < files.size(); i++) {
			file = files.get(i);
			if(!file.exists()) {
                continue;
            }
			/** 压缩一个文件 */
			//compressFile(file, out, basedir + file + "/");
			compressFile(file, out, basedir);
		}
	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte[] data = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public File getZipFile() {
		return zipFile;
	}

	public void setZipFile(File zipFile) {
		this.zipFile = zipFile;
	}

	public static void main(String[] args) {
//		ZipUtil zc = new ZipUtil("C:/FetionBox.zip");
//		zc.compress("C:/FetionBox"); // 压缩一个文件夹
	}
}
