package mapreduce.test;

import java.util.Date;
import java.util.HashMap;

/**
 * 位置信息汇总信息
 * ClassName: LocationBean 
 * date: 2014年12月2日 上午10:50:34 
 *
 * @author sid
 */
public class YyxlCl{

	private Date nowDate;//插入时间
	private String xzqhdm;//行政区划代码
	private int emptyCarCount;//空车数量
	private int onlineCarCount;//在线数量
	private int loadedCarCount;//重车数量
	private int stopedCarCount;//停运数量
	private double aveSpeed;//平均速度

	public String getXzqhdm() {
		return xzqhdm;
	}

	public void setXzqhdm(String xzqhdm) {
		this.xzqhdm = xzqhdm;
	}
	
	public Date getNowDate() {
		return nowDate;
	}

	public void setNowDate(Date nowDate) {
		this.nowDate = nowDate;
	}

	public int getEmptyCarCount() {
		return emptyCarCount;
	}

	public void setEmptyCarCount(int emptyCarCount) {
		this.emptyCarCount = emptyCarCount;
	}

	public int getOnlineCarCount() {
		return onlineCarCount;
	}

	public void setOnlineCarCount(int onlineCarCount) {
		this.onlineCarCount = onlineCarCount;
	}

	public int getLoadedCarCount() {
		return loadedCarCount;
	}

	public void setLoadedCarCount(int loadedCarCount) {
		this.loadedCarCount = loadedCarCount;
	}

	public int getStopedCarCount() {
		return stopedCarCount;
	}

	public void setStopedCarCount(int stopedCarCount) {
		this.stopedCarCount = stopedCarCount;
	}

	public double getAveSpeed() {
		return aveSpeed;
	}

	public void setAveSpeed(double aveSpeed) {
		this.aveSpeed = aveSpeed;
	}

}
