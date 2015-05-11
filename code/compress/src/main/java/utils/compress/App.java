package utils.compress;

import java.io.File;

import org.slf4j.Logger;

import utils.compress.apache.ZipCompressor;
import utils.compress.mongo.ConfigMongo;
import utils.compress.mongo.DropDatabase;
import utils.utils.DataTypeUtil;
import utils.utils.LogUtil;

/**
 * 
 * ClassName: App 
 * date: 2015年4月24日 上午10:57:52 
 *
 * @author sid
 */
public class App {
	
	private static Logger logger = LogUtil.getInstance().getLogger(App.class);
	
	public static void main(String[] args) {
		if (args.length!=1) {
			logger.error("请指定备份的截止日期");
		}else{
			String now = args[0];
			if (DataTypeUtil.isValidDate(now, "yyyyMMdd")) {
				//1.删除数据库
				DropDatabase.dropDatabase(now);
				//2.打包数据
				if ("1".equals(ConfigMongo.COMPRESS)) {
					File dir = new File(ConfigMongo.DBS_PATH);
					File[] listFiles = dir.listFiles();
					for (File file : listFiles) {
						try {
							if (!ConfigMongo.DB_NOT_BAK.contains(file.getName())) {
								String target = ConfigMongo.DBS_BAKPATH + File.separator + file.getName() + ".zip";
								String source = ConfigMongo.DBS_PATH + File.separator + file.getName();
								ZipCompressor zip = new ZipCompressor(target);
								zip.compress(source);
								logger.info(ConfigMongo.DBS_BAKPATH + File.separator + file.getName() + "数据库打包完成");
							}else{
								logger.info(ConfigMongo.DBS_BAKPATH + File.separator + file.getName() + "数据库不需要打包");
							}
						} catch (Exception e) {
							logger.info(ConfigMongo.DBS_BAKPATH + File.separator + file.getName() + "数据库打包失败");
							e.printStackTrace();
						}
					}
				}
			}else{
				logger.error("截止日期格式为：yyyyMMdd");
			}
		}
	}
}
