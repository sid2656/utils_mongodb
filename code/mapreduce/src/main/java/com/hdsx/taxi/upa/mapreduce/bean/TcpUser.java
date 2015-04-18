package com.hdsx.taxi.upa.mapreduce.bean;

import java.util.HashMap;

/**
 * Tcp用户实体
 * 
 * @author cuipengfei
 *
 */
public class TcpUser extends BaseBean {

	/**
	 * 用户id
	 */
	private int userid;

	/**
	 * 密码（明文）
	 */
	private String password;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 接入码
	 */
	private long msg_gnsscenterid;

	private String xzqhdm;
	private long M1;
	private long IA1;
	private long IC1;

	public String getXzqhdm() {
		return xzqhdm;
	}

	public void setXzqhdm(String xzqhdm) {
		this.xzqhdm = xzqhdm;
	}

	public long getM1() {
		return M1;
	}

	public void setM1(long m1) {
		M1 = m1;
	}

	public long getIA1() {
		return IA1;
	}

	public void setIA1(long iA1) {
		IA1 = iA1;
	}

	public long getIC1() {
		return IC1;
	}

	public void setIC1(long iC1) {
		IC1 = iC1;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getMsg_gnsscenterid() {
		return msg_gnsscenterid;
	}

	public void setMsg_gnsscenterid(long msg_gnsscenterid) {
		this.msg_gnsscenterid = msg_gnsscenterid;
	}

	@Override
	public HashMap<String, Object> getMapId() {

		// TODO Auto-generated method stub
		return null;
	}

}
