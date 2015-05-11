/**
 * Project Name:compress
 * File Name:s.java
 * Package Name:com.hdsx.taxi.upa.compress.utils
 * Date:2015年4月23日下午4:15:17
 * Copyright (c) 2015, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.compress.apache;
/**
 * ClassName:s
 * Date:     2015年4月23日 下午4:15:17 
 * @author   sid
 * @see 	 
 */

import java.io.File;
import java.util.Date;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipCompressorByAnt {
	
	public static Logger logger = LoggerFactory.getLogger(ZipCompressorByAnt.class);

	private File zipFile;

	public ZipCompressorByAnt(String pathName) {
		zipFile = new File(pathName);
	}
	
	public void compress(String srcPathName) {
		File srcdir = new File(srcPathName);

		if (!srcdir.exists()){
			logger.info(srcPathName + "不存在！");
		}else{
			try {
				Project prj = new Project();
				Zip zip = new Zip();
				zip.setProject(prj);
				zip.setDestFile(zipFile);
				FileSet fileSet = new FileSet();
				fileSet.setProject(prj);
				if (srcdir.isDirectory()) {
					logger.info("压缩文件夹：" + srcPathName + srcdir.getName());
					fileSet.setDir(srcdir);
				}else{
					logger.info("压缩文件：" + srcPathName + srcdir.getName());
					fileSet.setFile(srcdir);
				}
				//fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
				//fileSet.setExcludes(...); 排除哪些文件或文件夹
				zip.addFileset(fileSet);
				
				zip.execute();
				logger.info("压缩完成--"+DateUtils.format(new Date(), "yyyyMMddHHmmss"));
				boolean delete = srcdir.delete();
				logger.info("删除源文件--"+(delete?"成功":"失败"));
			} catch (BuildException e) {
				logger.error("压缩失败：", e);
				e.printStackTrace();
			}
		}
	}
}

