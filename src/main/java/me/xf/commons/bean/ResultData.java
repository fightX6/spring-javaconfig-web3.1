/**
 * 
 */
package me.xf.commons.bean;

import java.util.UUID;

/**
 * ResultData
 * @author xf
 * 2016年8月7日下午2:27:50
 */
public class ResultData {
	private String code;
	private long time ;
	private String msg;
	
	/**
	 * @param time
	 * @param b
	 * @param message
	 */
	public ResultData(long time, String msg) {
		this.time = time;
		this.msg = msg;
		this.code = UUID.randomUUID().toString();
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
}
