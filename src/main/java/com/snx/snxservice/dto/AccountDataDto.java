package com.snx.snxservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Rohit Lakshykar
 */
@JsonPropertyOrder({ "account_name", "hits" })
public class AccountDataDto {

	@JsonProperty("account_name")
	String accountName;

	int hits;

	/**
	 * @param count
	 * @param string
	 */
	public AccountDataDto(String key, Integer count) {
		this.accountName = key;
		this.hits = count;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
	

}
