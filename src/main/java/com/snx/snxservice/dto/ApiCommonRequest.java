package com.snx.snxservice.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiCommonRequest {

	Map<String, MultipartFile> files;

	Map<String, String> requestParameters;

	public Map<String, MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(Map<String, MultipartFile> files) {
		this.files = files;
	}

	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}

}
