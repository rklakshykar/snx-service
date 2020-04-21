package com.snx.snxservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Rohit Lakshykar
 */
@JsonPropertyOrder({ "activity", "hits" })
public class ActivityDataDto {

	String activity;

	int hits;

	/**
	 * @param hits
	 * @param string
	 */
	public ActivityDataDto(String activity, Integer hits) {
		this.activity = activity;
		this.hits = hits;
	}

	public String getActivity() {
		return activity;
	}

	public int getHits() {
		return hits;
	}

}
