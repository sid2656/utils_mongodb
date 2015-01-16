package utils.db.mongodb.init;

import utils.utils.PropertiesUtil;



public class InitMongo {
	
	/**
	 * 系统根目录
	 */
	public static final String ROOT_PATH = System.getProperty("user.dir");
	
	/**
	 * 配置文件地址
	 */
	private static final String config="/db.properties";
	private static final String charset="utf-8";
	public static final int SERVER_PORT = Integer.parseInt(PropertiesUtil.getProperties(config, charset).getProperty("mongodb.port"));
	public static final String SERVER_HOST =PropertiesUtil.getProperties(config, charset).getProperty("mongodb.ip");
	public static final String SERVER_HOSTS =PropertiesUtil.getProperties(config, charset).getProperty("mongodb.ips");
	public static final String DATABASE_INFO =PropertiesUtil.getProperties(config, charset).getProperty("database.info");
	public static final String DATABASE_MAKE =PropertiesUtil.getProperties(config, charset).getProperty("database.make");
	public static final String DATABASE_TRACK =PropertiesUtil.getProperties(config, charset).getProperty("database.track");
	
}
