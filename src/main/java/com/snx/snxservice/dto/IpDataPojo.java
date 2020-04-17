package com.snx.snxservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Rohit Lakshykar
 */
@JsonPropertyOrder({ "ip", "requests" })
public class IpDataPojo {

	String ip;

	int requests;

	/**
	 * @param count
	 * @param string
	 */
	public IpDataPojo(String key, Integer count) {
		this.ip = key;
		this.requests = count;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String data) {
		this.ip = data;
	}

	public int getRequests() {
		return requests;
	}

	public void setRequests(int requests) {
		this.requests = requests;
	}
	
	

}
