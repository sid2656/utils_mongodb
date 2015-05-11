/**
 * Project Name:compress
 * File Name:TestZip.java
 * Package Name:com.hdsx.taxi.upa.compress
 * Date:2015年4月23日下午4:17:52
 * Copyright (c) 2015, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.compress;

import utils.compress.apache.ZipCompressor;
import utils.compress.apache.ZipCompressorByAnt;

/**
 * ClassName:TestZip
 * Date:     2015年4月23日 下午4:17:52 
 * @author   sid
 * @see 	 
 */

public class TestZip {
	public static void main(String[] args) {
		ZipCompressor zc = new  ZipCompressor("D://sid//work//workspace_git//upa//upa//city_data//szhzip.zip");
		zc.compress("D://sid//work//workspace_git//upa//upa//city_data//20150104_cq_t_taxi_cyryxx.json");
		
		ZipCompressorByAnt zca = new ZipCompressorByAnt("D://sid//work//workspace_git//upa//upa//city_data//szhzipant.zip");
		zca.compress("D://sid//work//workspace_git//upa//upa//city_data//20150104_cq_t_taxi_cyryxx.json");
	}
}