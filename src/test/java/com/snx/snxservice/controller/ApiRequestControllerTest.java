package com.snx.snxservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snx.snxservice.dto.ApiCommonResponse;

/**
 * @author Rohit Lakshykar
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApiRequestControllerTest {

	TestInfo testInfo;
	TestReporter testReporter;

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	int randomServerPort;

	@BeforeEach

	void init(TestInfo testInfo, TestReporter testReporter) {
		this.testInfo = testInfo;
		this.testReporter = testReporter;
	}

	@Test
	@DisplayName("File upload Rest API test /api/v1/fileupload")
	public void testAddEmployeeSuccess() throws URISyntaxException {
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("file", new org.springframework.core.io.ClassPathResource("testfile.csv"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				parameters, headers);

		ResponseEntity<ApiCommonResponse> response = testRestTemplate.exchange("/api/v1/fileupload", HttpMethod.POST,
				entity, ApiCommonResponse.class, "");
		try {
			testReporter.publishEntry(mapper.writeValueAsString(response.getBody()));
		} catch (JsonProcessingException e) {
			testReporter.publishEntry(e.getMessage());
		}

		assertAll(() -> assertThat(response.getStatusCode(), is(HttpStatus.OK)),
				() -> assertEquals(response.getBody().getStatus(), 0, () -> "Status should be 0"),
				() -> assertNotNull(response.getBody().getData(), () -> "Data should not be null"));
	}

}
