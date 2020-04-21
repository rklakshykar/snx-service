package com.snx.snxservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snx.snxservice.dto.ApiCommonRequest;
import com.snx.snxservice.dto.ApiCommonResponse;
import com.snx.snxservice.util.StatusConstants;

/**
 * @author Rohit Lakshykar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class FileProcessingServiceTest {

	ApiCommonRequest request;
	TestInfo testInfo;
	TestReporter testReporter;

	@Autowired
	FileProcessingService fileProcessingService;

	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void init(TestInfo testInfo, TestReporter testReporter) {
		this.request = new ApiCommonRequest();
		this.testInfo = testInfo;
		this.testReporter = testReporter;
	}

	@Test
	@DisplayName("File Upload Test")
	@Tag("Critical")
	void fileUploadTest() {
		MultipartFile multipartFile;
		try {
			multipartFile = new MockMultipartFile("testfile.csv", "testfile.csv", "text/plain",
					new FileInputStream(new File(getClass().getResource("/testfile.csv").getPath())));

			ApiCommonResponse response = fileProcessingService.processFile(request, multipartFile);
			testReporter.publishEntry(mapper.writeValueAsString(response));

			assertAll(
					() -> assertEquals(response.getStatus(), StatusConstants.SUCCESS.getId(),
							() -> "Status should be 0"),
					() -> assertNotNull(response.getData(), () -> "Data should not be null"));

		} catch (FileNotFoundException e) {
			fail("Exception should not be thrown");
		} catch (IOException e) {
			fail("Exception should not be thrown");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception should not be thrown");
		}
	}

}
