package com.snx.snxservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Rohit Lakshykar
 */
@JsonPropertyOrder({ "activity", "requests" })
public class ActivityDataPojo {

	String activity;

	@JsonProperty("hits")
	int requests;

	/**
	 * @param count
	 * @param string
	 */
	public ActivityDataPojo(String activity, Integer count) {
		this.activity = activity;
		this.requests = count;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public int getHits() {
		return requests;
	}

	public void setRequests(int requests) {
		this.requests = requests;
	}

}
