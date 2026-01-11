package com.library.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.service.PdfCreaterService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/api/pdf")
@RestController
public class PdfGeneratorController {
	@Autowired
	private PdfCreaterService pdfCreaterService;

	@PostMapping("/create")
	public ResponseEntity<byte[]> createPdf(@RequestBody JsonNode payload) {
		
		
		byte[] pdfBytes = pdfCreaterService.createPdf(payload);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.attachment().filename("report.pdf").build());

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

}
