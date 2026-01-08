package com.library.controller.admin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.UserDTO;
import com.library.entity.Librarian;
import com.library.entity.LoginUser;
import com.library.entity.LoginUserDetails;
import com.library.service.LoginRolesService;
import com.library.service.LoginUserService;
import com.library.service.admin.AdminLibrarianService;

import jakarta.validation.Valid;
import tools.jackson.databind.JsonNode;

@RequestMapping("/admin/api/manage-librarian")
@RestController
public class AdminManageLibrarianController {
	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private AdminLibrarianService librarianService;
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
			if (libr.getId() == null) {
				libr.setCreatedBy(((LoginUserDetails) usr).getUser());
			}
			libr.setUpdatedBy(((LoginUserDetails) usr).getUser());
			try {
				libr = librarianService.saveLibrarian(libr);
			} catch (Exception ex) {
				ret.put("successfull", false);
			}
			ret.put("successfull", libr.getId() != null);
		} else {
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

	@PutMapping("/{id}")
	@Valid
	public Map<String, Boolean> updateLibrarian(@Valid @PathVariable Long id, @RequestBody JsonNode jsonNode,
			@AuthenticationPrincipal UserDetails usr) {
		Librarian librarian = librarianService.findById(id).get();
		Map<String, Boolean> ret = new HashMap<>();
		ret.put("successfull", false);
		if (librarian != null) {
			librarian.getLoginUser().setEnabled(jsonNode.get("enabled").asBoolean());
			librarian.getLoginUser().setName(jsonNode.get("name").asString());
			if (!jsonNode.get("password").asString().isBlank()) {
				librarian.getLoginUser().setPassword(passwordEncoder.encode(jsonNode.get("password").asString()));
			}

			LoginUser loginUser = loginUserService.saveLoginUser(librarian.getLoginUser());
			librarian.setLegalName(jsonNode.get("legalName").asString());
			librarian.setLoginUser(loginUser);
			librarian.setAddress(jsonNode.get("address").asString());
			librarian.setEmail(jsonNode.get("email").asString());
			librarian.setPhoneNo(jsonNode.get("phone").asString());
			//librarian.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

			librarian.setUpdatedBy(((LoginUserDetails) usr).getUser());
			try {
				librarian = librarianService.saveLibrarian(librarian);
			} catch (Exception ex) {
				ret.put("successfull", false);
			}
			ret.put("successfull", librarian.getId() != null);
		}
		return ret;

	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteLibrarian(@PathVariable Long id) {
		boolean status = true;
		Librarian librarian = new Librarian();
		librarian.setId(id);
		Map<String, Boolean> ret = new HashMap<>();
		try {
			librarianService.deleteLibrarian(librarian);
		} catch (Exception ex) {
			status = false;
		}
		ret.put("successfull", status);
		return ret;

	}

}
