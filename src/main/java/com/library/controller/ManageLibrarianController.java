package com.library.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Librarian;
import com.library.entity.LoginUser;
import com.library.entity.LoginUserDetails;
import com.library.service.LibrarianService;
import com.library.service.LoginRolesService;
import com.library.service.LoginUserService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/admin/api/manage-librarian")
@RestController
public class ManageLibrarianController {
	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private LibrarianService librarianService;
	@Autowired
	private LoginRolesService loginRolesService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/save")
	public Map<String, Boolean> saveAdmin(@RequestBody JsonNode jsonNode, @AuthenticationPrincipal UserDetails usr) {
		LoginUser loginUser = new LoginUser();

		loginUser.setEnabled(jsonNode.get("enabled").asBoolean());
		loginUser.setName(jsonNode.get("name").asString());
		loginUser.setPassword(passwordEncoder.encode(jsonNode.get("password").asString()));
		loginUser.setRole(loginRolesService.findByName("LIBRARIAN").get());
		Map<String, Boolean> ret = new HashMap<>();
		ret.put("successfull", false);
		
		loginUser = loginUserService.saveLoginUser(loginUser);
		if (loginUser.getId() != null) {
			Librarian libr = new Librarian();
			libr.setLegalName(jsonNode.get("legalName").asString());
			libr.setLoginUser(loginUser);
			libr.setAddress(jsonNode.get("address").asString());
			libr.setEmail(jsonNode.get("email").asString());
			libr.setPhoneNo(jsonNode.get("phone").asString());
			libr.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
			libr.setCreatedBy(((LoginUserDetails) usr).getUser());
			libr.setUpdatedBy(((LoginUserDetails) usr).getUser());
			libr = librarianService.saveLibrarian(libr);
			ret.put("successfull", libr.getId() != null);
		}else {
			loginUserService.deleteLoginUser(loginUser);
		}
		return ret;
	}
	
	@PostMapping("/list")
	public PagedModel<Librarian> getListOfLibrarians(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianService.listAll(payload));

	}
	
	@PostMapping("/search")
	public PagedModel<Librarian> getListOfAdminsMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianService.findAll(payload));

	}

}
