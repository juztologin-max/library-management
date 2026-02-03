package com.library.controller.librarian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.projections.librarian.LibrarianDuesProjection;
import com.library.service.PdfCreaterService;
import com.library.service.librarian.LibrarianDuesService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/librarian/api/manage-dues")
@RestController
@PreAuthorize("hasAuthority('LIBRARIAN')")
public class LibrarianDuesController {
	@Autowired
	private LibrarianDuesService librarianDuesService;
	@Autowired
	private PdfCreaterService pdfCreaterService;

	@PostMapping("/list-dues")
	public PagedModel<LibrarianDuesProjection> listBorrowings(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianDuesService.listAll(payload));

	}

	@PostMapping("/pdf/create")
	public ResponseEntity<byte[]> createPdf(@RequestBody JsonNode payload) {

		byte[] pdfBytes = pdfCreaterService.createPdf(payload);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.attachment().filename("dues.pdf").build());

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

}
