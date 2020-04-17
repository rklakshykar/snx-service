package com.snx.snxservice.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snx.snxservice.dto.ApiCommonRequest;
import com.snx.snxservice.dto.ApiCommonResponse;
import com.snx.snxservice.service.FileProcessingService;

@Controller
public class UploadController {

	static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	FileProcessingService fileProcessingService;
	
	@GetMapping("/")
	public String index() {
		return "upload";
	}

	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam Map<String, String> requestParameters,@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("errormsg", "Please select a file to upload");
			return "redirect:/";
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		
		ApiCommonRequest request = new ApiCommonRequest();
		request.setRequestParameters(requestParameters);
		
		try {
			
			ApiCommonResponse response = fileProcessingService.processFile(request, file);
			
			logger.info("Responise :{}",mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
			
			/*redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileProcessingService.processFile(request, file)) + "'");
			*/
			redirectAttributes.addFlashAttribute("message",(response.getMessage()!=null?response.getMessage():response.getStatusDesc()));
						
			redirectAttributes.addFlashAttribute("response", response);
			
		} catch (Exception e) {
		logger.error("Exception",e);
			redirectAttributes.addFlashAttribute("errormsg",
					"Exception");
			redirectAttributes.getFlashAttributes().remove("message");

		}

		return "redirect:/";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

}