/**
 * Project Name:compress
 * File Name:s.java
 * Package Name:com.hdsx.taxi.upa.compress.utils
 * Date:2015年4月23日下午4:08:06
 * Copyright (c) 2015, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.compress.apache;
/**
 * ClassName:s
 * Date:     2015年4月23日 下午4:08:06 
 * @author   sid
 * @see 	 
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZipCompressor {
	
	public static Logger logger = LoggerFactory.getLogger(ZipCompressor.class);
	
	static final int BUFFER = 8192;

	private File zipFile;

	public ZipCompressor(String pathName) {
		zipFile = new File(pathName);
	}

	public void compress(String srcPathName) {
		File file = new File(srcPathName);
		if (!file.exists()){
			logger.info(srcPathName + "不存在！");
		}else{
			FileOutputStream fileOutputStream = null;
			CheckedOutputStream cos = null;
			ZipOutputStream out = null;
			try {
				if (zipFile.exists()) {
					boolean delete = zipFile.delete();
					logger.info(zipFile.getName()+"文件存在，删除状态："+delete);
				}
				fileOutputStream = new FileOutputStream(zipFile);
				cos = new CheckedOutputStream(fileOutputStream,
						new CRC32());
				out = new ZipOutputStream(cos);
				String basedir = "";
				compress(file, out, basedir);
				logger.info("压缩完成--"+DateUtils.format(new Date(), "yyyyMMddHHmmss"));
				boolean delete = file.delete();
				logger.info("删除源文件--"+(delete?"成功":"失败"));
			} catch (Exception e) {
				logger.error("压缩失败：", e);
				e.printStackTrace();
			}finally{
				try {
					if (out!=null) {
						out.close();
					}
					if (fileOutputStream!=null) {
						fileOutputStream.close();
					}
					if (cos!=null) {
						cos.close();
					}
				} catch (IOException e) {
					logger.error("关闭流失败：", e);
					e.printStackTrace();
				}
			}
		}
	}

	private void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			logger.info("压缩文件夹：" + basedir + file.getName());
			this.compressDirectory(file, out, basedir);
		} else {
			logger.info("压缩文件：" + basedir + file.getName());
			this.compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}