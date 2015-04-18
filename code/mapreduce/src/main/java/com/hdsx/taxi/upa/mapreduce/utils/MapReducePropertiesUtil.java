package com.hdsx.taxi.upa.mapreduce.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ClassName: PropertiesUtil 
 * Reason: 读取配置文件. 
 * date: 2014年12月1日 上午10:05:58 
 *
 * @author sid
 */
public class MapReducePropertiesUtil {

	private static final Logger logger = LoggerFactory.getLogger(MapReducePropertiesUtil.class);

	private static Properties p;

	public static Properties getProperties() {
		if (p == null) {
			p = new Properties();
			try {
				p.load(MapReducePropertiesUtil.class
						.getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				logger.error("配置文件config.properties初始化错误：" + e.getMessage());
			}
		}
		return p;
	}
}
