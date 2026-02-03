package com.library.controller.librarian;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.library.projections.librarian.LibrarianDuesProjection;
import com.library.service.librarian.LibrarianDuesService;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.ArrayNode;

@RequestMapping("/librarian/api/manage-dues")
@RestController
@PreAuthorize("hasAuthority('LIBRARIAN')")
public class LibrarianDuesController {

	private final LibrarianDuesService librarianDuesService;
	private final RestTemplate rest;
	private final String pdfServiceUrl;

	@Autowired
	public LibrarianDuesController(LibrarianDuesService librarianDuesService, RestTemplate rest,
			@Value("${pdf-creation-url}") String pdfServiceUrl) {
		this.librarianDuesService = librarianDuesService;
		this.rest = rest;
		this.pdfServiceUrl = pdfServiceUrl;
	}

	@PostMapping("/list-dues")
	public PagedModel<LibrarianDuesProjection> listBorrowings(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianDuesService.listAll(payload));

	}

	@GetMapping("/pdf/create")
	public ResponseEntity<byte[]> createPdf() {
		Map<String, String> hmap = new HashMap<>();
		hmap.put("BOOK", "book");
		hmap.put("BORROWER", "borrower");
		hmap.put("BORROW DATE", "borrowDate");
		hmap.put("DUE", "due");
		String title = "Dues";

		JsonNode postNode = new ObjectMapper().readTree("""
				{
					"pageable": {
						"pageNo": 0,
						"pageSize": 10000,
						"sortable": {
						    "borrowDate":"ASC"
						}
					}
				}
						""");

		Page<LibrarianDuesProjection> page = librarianDuesService.listAll(postNode);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode contentNode = mapper.createObjectNode();
		contentNode.put("title", title);
		contentNode.set("headers", mapper.convertValue(hmap, ObjectNode.class));
		contentNode.set("rows", mapper.convertValue(page.getContent(), ArrayNode.class));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ObjectNode> ent = new HttpEntity<>(contentNode, headers);
		byte[] pdfBytes = rest.postForObject(pdfServiceUrl, ent, byte[].class);

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.attachment().filename("dues.pdf").build());

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

}
