package com.snx.snxservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snx.snxservice.dto.AccountDataPojo;

/**
 * @author Rohit Lakshykar
 */
public class TopAccountThread implements Callable<List<Object>> {

	static final Logger logger = LoggerFactory.getLogger(TopAccountThread.class);

	Map<String, Integer> accountsMap;

	int maxAccounts;

	public TopAccountThread(Map<String, Integer> accountsMap, int maxAccounts) {
		super();
		this.accountsMap = accountsMap;
		this.maxAccounts = maxAccounts;
	}

	@Override
	public List<Object> call() throws Exception {
		logger.info("Accounts size: {}", accountsMap.size());
		List<AccountDataPojo> accountDataList = new ArrayList<>();
		accountsMap.entrySet()
				.forEach(entry -> accountDataList.add(new AccountDataPojo(entry.getKey(), entry.getValue())));
		return getTopXAccounts(accountDataList, maxAccounts);
	}

	private List<Object> getTopXAccounts(List<AccountDataPojo> dataList, int maxValues) {

		JSONArray accountList = new JSONArray();

		Collections.sort(dataList, (o1, o2) -> {
			return o1.getRequests() < o2.getRequests() ? 1 : o1.getRequests() > o2.getRequests() ? -1 : 0;
		});

		int count = 0;
		for (AccountDataPojo data : dataList) {
			if (count == maxValues)
				break;
			count++;
			JSONObject obj = new JSONObject();
			obj.put("account_name", data.getAccountName());
			obj.put("hits", data.getRequests());
			accountList.put(obj);
		}

		return accountList.toList();
	}

}
