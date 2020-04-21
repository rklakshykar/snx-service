package com.snx.snxservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snx.snxservice.dto.IpDataDto;

/**
 * @author Rohit Lakshykar
 */
public class TopIpsThread implements Callable<List<IpDataDto>> {

	static final Logger logger = LoggerFactory.getLogger(TopIpsThread.class);

	Map<String, Integer> ipsMap;

	int maxIps;

	public TopIpsThread(Map<String, Integer> ipsMap, int maxIps) {
		super();
		this.ipsMap = ipsMap;
		this.maxIps = maxIps;
	}

	@Override
	public List<IpDataDto> call() throws Exception {
		logger.info("Ips size: {}", ipsMap.size());
		List<IpDataDto> ipDataList = new ArrayList<>();
		ipsMap.entrySet().forEach(entry -> ipDataList.add(new IpDataDto(entry.getKey(), entry.getValue())));
		return getTopXIps(ipDataList, this.maxIps);
	}

	private List<IpDataDto> getTopXIps(List<IpDataDto> dataList, int maxIps) {

		Collections.sort(dataList, (o1, o2) -> o1.getHits() < o2.getHits() ? 1 : o1.getHits() > o2.getHits() ? -1 : 0);

		if (maxIps > 0) {
			return dataList.stream().limit(maxIps).collect(Collectors.toList());
		} else {
			return dataList;
		}

	}

}
