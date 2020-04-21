package com.snx.snxservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Rohit Lakshykar
 */
@JsonPropertyOrder({ "account_name", "hits" })
public class AccountDataDto {

	String account_name;

	int hits;

	/**
	 * @param count
	 * @param string
	 */
	public AccountDataDto(String key, Integer count) {
		this.account_name = key;
		this.hits = count;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccountName(String accountName) {
		this.account_name = accountName;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
	

}
