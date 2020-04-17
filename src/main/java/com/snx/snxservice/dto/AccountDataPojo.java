package com.snx.snxservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Rohit Lakshykar
 */
@JsonPropertyOrder({ "account_name", "requests" })
public class AccountDataPojo {

	@JsonProperty("account_name")
	String accountName;

	int requests;

	/**
	 * @param count
	 * @param string
	 */
	public AccountDataPojo(String key, Integer count) {
		this.accountName = key;
		this.requests = count;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getRequests() {
		return requests;
	}

	public void setRequests(int requests) {
		this.requests = requests;
	}
	
	

}
