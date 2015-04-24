package utils.db.mongobak;

import utils.utils.PropertiesUtil;



public class ConfigMongo {
	
	/** 系统根目录 */
	public static final String ROOT_PATH = System.getProperty("user.dir");
	/** 配置文件地址 */
	private static final String configFileName="compress.properties";
	/** 不需要备份的数据库 **/
	public static final String DB_NOT_BAK = PropertiesUtil.getProperties(configFileName).getProperty("donot.backup");
	/** 需删除的数据库前缀 **/
	public static final String DB_DEL_PRE = PropertiesUtil.getProperties(configFileName).getProperty("delete.prefix");
	/** 数据库列表文件夹 **/
	public static final String DBS_PATH = PropertiesUtil.getProperties(configFileName).getProperty("databases.path");
	/** 数据库列表备份存放 **/
	public static final String DBS_BAKPATH = PropertiesUtil.getProperties(configFileName).getProperty("databases.bakpath");
	
}
