package com.snx.snxservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Rohit Lakshykar
 */
@JsonPropertyOrder({ "ip", "hits" })
public class IpDataDto {

	String ip;

	int hits;

	/**
	 * @param hits
	 * @param string
	 */
	public IpDataDto(String ip, Integer hits) {
		this.ip = ip;
		this.hits = hits;
	}

	public String getIp() {
		return ip;
	}

	public int getHits() {
		return hits;
	}

}
