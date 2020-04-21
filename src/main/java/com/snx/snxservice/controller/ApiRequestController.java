package com.snx.snxservice.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snx.snxservice.dto.ApiCommonRequest;
import com.snx.snxservice.dto.ApiCommonResponse;
import com.snx.snxservice.service.FileProcessingService;

/**
 * @Author Rohit Lakshykar 24-Apr-2018 10:44:13 AM
 *
 */

@Controller
@RequestMapping("/api/v1/")
public class ApiRequestController {

	static final Logger logger = LoggerFactory.getLogger(ApiRequestController.class);

	@Autowired
	FileProcessingService fileProcessingService;

	public static final ObjectMapper mapper = new ObjectMapper();

	@PostMapping(value = "/fileupload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> captureNgoCustomer(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {

		ApiCommonRequest request = new ApiCommonRequest();
		request.setRequestParameters(requestParameters);
		
		ApiCommonResponse response = fileProcessingService.processFile(request,file);

		return ResponseEntity.status(response.getHttpCode()).body(response);
	}

	
	
}