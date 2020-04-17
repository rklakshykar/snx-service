package com.snx.snxservice.util;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum StatusConstants {

	SUCCESS(0, "Success"), FAIL(1, "Fail"), ERROR(2, "Error"),;

	private int id;

	private String description;

	private StatusConstants(int id, String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	void setDescription(String description) {
		this.description = description;
	}

}
