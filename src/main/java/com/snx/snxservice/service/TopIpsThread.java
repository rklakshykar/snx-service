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

import com.snx.snxservice.dto.IpDataPojo;

/**
 * @author Rohit Lakshykar
 */
public class TopIpsThread implements Callable<List<Object>> {

	static final Logger logger = LoggerFactory.getLogger(TopIpsThread.class);

	Map<String, Integer> ipsMap;

	int maxIps;

	public TopIpsThread(Map<String, Integer> ipsMap, int maxIps) {
		super();
		this.ipsMap = ipsMap;
		this.maxIps = maxIps;
	}

	@Override
	public List<Object> call() throws Exception {
		logger.info("Ips size: {}", ipsMap.size());
		List<IpDataPojo> ipDataList = new ArrayList<>();
		ipsMap.entrySet().forEach(entry -> ipDataList.add(new IpDataPojo(entry.getKey(), entry.getValue())));
		return getTopXIps(ipDataList, maxIps);
	}

	private List<Object> getTopXIps(List<IpDataPojo> dataList, int maxValues) {

		JSONArray ipList = new JSONArray();
		
		Collections.sort(dataList, (o1, o2) -> {
			return o1.getRequests() < o2.getRequests() ? 1 : o1.getRequests() > o2.getRequests() ? -1 : 0;
		});

		int count = 0;

		for (IpDataPojo data : dataList) {
			if (count == maxValues)
				break;
			count++;
			JSONObject obj = new JSONObject();
			obj.put("ip", data.getIp());
			obj.put("hits", data.getRequests());
			ipList.put(obj);
		}

		return ipList.toList();
	}

}
