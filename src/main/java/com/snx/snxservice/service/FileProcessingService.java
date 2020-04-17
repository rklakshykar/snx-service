package com.snx.snxservice.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.snx.snxservice.dto.ApiCommonRequest;
import com.snx.snxservice.dto.ApiCommonResponse;
import com.snx.snxservice.util.StatusConstants;

/**
 * @author Rohit Lakshykar
 */

@Service
public class FileProcessingService {

	static final Logger logger = LoggerFactory.getLogger(FileProcessingService.class);

	@Autowired
	Environment env;

	ExecutorService executorService = Executors.newFixedThreadPool(3);

	/**
	 * @param request
	 * @param file
	 * @return
	 */
	public ApiCommonResponse processFile(ApiCommonRequest request, MultipartFile file) {

		logger.info("Inside processFile of {}  : filename: {}", getClass().getSimpleName(), file.getOriginalFilename());

		ApiCommonResponse response = new ApiCommonResponse();
		if (file.isEmpty()) {
			response.setStatus(StatusConstants.FAIL.getId());
			response.setStatusDesc(StatusConstants.FAIL.getDescription());
			response.setMessage("file required");
			response.setHttpCode(HttpStatus.BAD_REQUEST.value());
			return response;
		}

		Instant start = Instant.now();

		DateTimeFormatter formatter = getDateFormatter();

		Map<String, Object> responseParameters = new HashMap<>();

		createDirectoryIfNotExists();

		StringBuilder fileName = createFileName(file);

		Map<String, Integer> ipsMap = new HashMap<>();

		Map<String, Integer> accountsMap = new HashMap<>();

		Map<Integer, Map<String, Integer>> hourlyActivities = new HashMap<>();

		try {
			file.transferTo(new File(fileName.toString()));

			try (Stream<String> lines = Files.lines(Paths.get(fileName.toString()))) {
				lines.forEach(line -> parseAndFillMaps(formatter, ipsMap, accountsMap, hourlyActivities, line));
			}

			responseParameters.put("unique_ips", ipsMap.size());
			responseParameters.put("unique_accounts", accountsMap.size());

			Future<List<Object>> topIpsList = executorService.submit(new TopIpsThread(ipsMap, 5));
			Future<List<Object>> topAccountList = executorService.submit(new TopAccountThread(accountsMap, 5));
			Future<List<Object>> actilityList = executorService.submit(new ActivityThread(hourlyActivities));

			responseParameters.put("top_ips", topIpsList.get());
			responseParameters.put("top_accounts", topAccountList.get());
			responseParameters.put("hourly_activity_list", actilityList.get());

			Instant finish = Instant.now();

			long timeElapsed = Duration.between(start, finish).toMillis();

			logger.info("time to process file in millis:{}", timeElapsed);

			response.setData(responseParameters);
			response.setStatus(StatusConstants.SUCCESS.getId());
			response.setStatusDesc(StatusConstants.SUCCESS.getDescription());
			response.setMessage("File uploaded and processed");
			response.setHttpCode(HttpStatus.OK.value());
		} catch (Exception e) {
			logger.error("Exception", e);
			response.setStatus(StatusConstants.FAIL.getId());
			response.setStatusDesc(StatusConstants.FAIL.getDescription());
			response.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage("Some error occured file processing the file, Please check the file and reupload");
		}

		return response;
	}

	private void parseAndFillMaps(DateTimeFormatter formatter, Map<String, Integer> ipsMap,
			Map<String, Integer> accountsMap, Map<Integer, Map<String, Integer>> hourlyActivities, String line) {

		try {
			if (line == null || "".equals(line)) {
				return;
			}

			String[] lineData = line.split("\\|");

			if (ipsMap.get(lineData[7]) != null) {
				ipsMap.put(lineData[7], ipsMap.get(lineData[7]) + 1);
			} else {
				ipsMap.put(lineData[7], 1);
			}

			if (accountsMap.get(lineData[1]) != null) {
				accountsMap.put(lineData[1], accountsMap.get(lineData[1]) + 1);
			} else {
				accountsMap.put(lineData[1], 1);
			}

			int hour = LocalDateTime.parse(lineData[11], formatter).getHour();

			if (hourlyActivities.get(hour) != null) {
				Map<String, Integer> activityMap = hourlyActivities.get(hour);

				if (activityMap.get(lineData[9]) != null) {
					activityMap.put(lineData[9], activityMap.get(lineData[9]) + 1);
				} else {
					activityMap.put(lineData[9], 1);
				}

				hourlyActivities.put(hour, activityMap);
			} else {
				Map<String, Integer> activityMap = new HashMap<>();
				activityMap.put(lineData[9], 1);
				hourlyActivities.put(hour, activityMap);
			}
		} catch (Exception e) {
			logger.error("Exception while processing line : {}", line);
		}
	}

	private DateTimeFormatter getDateFormatter() {

		return new DateTimeFormatterBuilder().appendOptional(DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm:ss"))
				.appendOptional(DateTimeFormatter.ofPattern("M/dd/yyyy HH:m:ss"))
				.appendOptional(DateTimeFormatter.ofPattern("M/dd/yyyy HH:m:s"))
				.appendOptional(DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm:s"))
				.appendOptional(DateTimeFormatter.ofPattern(("MM/d/yyyy HH:mm:ss"))).toFormatter();

	}

	private StringBuilder createFileName(MultipartFile file) {
		return new StringBuilder(env.getProperty("spring.servlet.multipart.location"))
				.append(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.')))
				.append("_").append(System.currentTimeMillis())
				.append(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));
	}

	private void createDirectoryIfNotExists() {
		File dir = new File(env.getProperty("spring.servlet.multipart.location"));
		if (!dir.exists())
			dir.mkdir();
	}
}
