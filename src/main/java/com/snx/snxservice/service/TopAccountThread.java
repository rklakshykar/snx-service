package com.snx.snxservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snx.snxservice.dto.AccountDataDto;

/**
 * @author Rohit Lakshykar
 */
public class TopAccountThread implements Callable<List<AccountDataDto>> {

	static final Logger logger = LoggerFactory.getLogger(TopAccountThread.class);

	Map<String, Integer> accountsMap;

	int maxAccounts;

	public TopAccountThread(Map<String, Integer> accountsMap, int maxAccounts) {
		super();
		this.accountsMap = accountsMap;
		this.maxAccounts = maxAccounts;
	}

	@Override
	public List<AccountDataDto> call() throws Exception {
		logger.info("Accounts size: {}", accountsMap.size());
		List<AccountDataDto> accountDataList = new ArrayList<>();
		accountsMap.entrySet()
				.forEach(entry -> accountDataList.add(new AccountDataDto(entry.getKey(), entry.getValue())));
		return getTopXAccounts(accountDataList);
	}

	private List<AccountDataDto> getTopXAccounts(List<AccountDataDto> dataList) {

		Collections.sort(dataList, (o1, o2) -> o1.getHits() < o2.getHits() ? 1 : o1.getHits() > o2.getHits() ? -1 : 0);

		if (this.maxAccounts > 0) {
			return dataList.stream().limit(this.maxAccounts).collect(Collectors.toList());
		} else {
			return dataList;
		}

	}

}
