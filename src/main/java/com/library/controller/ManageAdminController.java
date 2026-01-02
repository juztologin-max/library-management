package com.library.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.UserDTO;
import com.library.entity.LoginUser;
import com.library.service.LoginRolesService;
import com.library.service.LoginUserService;

import jakarta.validation.Valid;
import tools.jackson.databind.JsonNode;

@RequestMapping("/admin/api/manage-admin")
@RestController
public class ManageAdminController {
	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private LoginRolesService loginRolesService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/save")
	public Map<String, Boolean> saveAdmin(@Valid @RequestBody UserDTO usr) {
		LoginUser loginUser = new LoginUser();

		loginUser.setEnabled(usr.isEnabled());
		loginUser.setName(usr.getName());
		loginUser.setPassword(passwordEncoder.encode(usr.getPassword()));

		loginUser.setRole(loginRolesService.findByName("ADMIN").get());
		Map<String, Boolean> ret = new HashMap<>();
		boolean status = loginUserService.saveLoginUser(loginUser) != null ;
		ret.put("successfull", status);
		return ret;
	}

	@PostMapping("/is-name-available")
	@Valid
	public Map<String, Boolean> isAdminNameAvailable(@RequestBody UserDTO usr) {

		boolean res = !loginUserService.findByName(usr.getName()).isPresent();
		Map<String, Boolean> map = new HashMap<>();
		map.put("is-name-available", res);
		return map;
	}

	@PostMapping("/list")
	public PagedModel<LoginUser> getListOfAdmins(@RequestBody JsonNode payload) {
		return new PagedModel<>(loginUserService.listAll(payload));

	}

	@PutMapping("/{id}")
	@Valid
	public Map<String, Boolean> updateAdmin(@Valid @PathVariable Long id, @RequestBody UserDTO usr) {
		LoginUser loginUser = loginUserService.findById(id).get();
		loginUser.setId(id);
		loginUser.setEnabled(usr.isEnabled());
		loginUser.setName(usr.getName());
		if (usr.getPassword() != null && !usr.getPassword().isEmpty()) {
			loginUser.setPassword(passwordEncoder.encode(usr.getPassword()));
		}
		// loginUser.setRole(loginRolesService.findByName("ADMIN").get());
		Map<String, Boolean> ret = new HashMap<>();
		boolean status = loginUserService.saveLoginUser(loginUser) != null;
		ret.put("successfull", status);
		return ret;

	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteAdmin(@PathVariable Long id) {
		boolean status = true;
		LoginUser loginUser = new LoginUser();
		loginUser.setId(id);
		Map<String, Boolean> ret = new HashMap<>();
		try {
			loginUserService.deleteLoginUser(loginUser);
		} catch (Exception ex) {
			status = false;
		}
		ret.put("successfull", status);
		return ret;

	}

	@PostMapping("/search")
	public PagedModel<LoginUser> getListOfAdminsMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(loginUserService.findAll(payload));

	}

}
