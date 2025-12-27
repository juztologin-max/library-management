package com.library.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.TableRequest;
import com.library.dto.UserDTO;
import com.library.entity.LoginUser;
import com.library.service.LoginRolesService;
import com.library.service.LoginUserService;

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
	public Map<String, Boolean> saveAdmin(@RequestBody UserDTO usr) {
		LoginUser loginUser = new LoginUser();

		loginUser.setEnabled(usr.isEnabled());
		loginUser.setName(usr.getName());
		loginUser.setPassword(passwordEncoder.encode(usr.getPassword()));

		loginUser.setRole(loginRolesService.findByName("ADMIN").get());
		Map<String, Boolean> ret = new HashMap<>();
		boolean status = loginUserService.saveLoginUser(loginUser) != null ? true : false;
		ret.put("successfull", status);
		return ret;
	}

	@PostMapping("/is-name-available")
	public Map<String, Boolean> isAdminNameAvailable(@RequestBody UserDTO usr) {

		boolean res = !loginUserService.findByName(usr.getName()).isPresent();
		Map<String, Boolean> map = new HashMap<>();
		map.put("is-name-available", res);
		return map;
	}

	@PostMapping("/list")
	public List<LoginUser> getListOfAdmins(@RequestBody TableRequest req) {
		List<LoginUser> list = loginUserService.listAll(req);
		return list;
	}

	@PutMapping("/{id}")
	public Map<String, Boolean> updateAdmin(@PathVariable Long id, @RequestBody UserDTO usr) {
		LoginUser loginUser = loginUserService.findById(id).get();
		loginUser.setId(id);
		loginUser.setEnabled(usr.isEnabled());
		loginUser.setName(usr.getName());
		if (usr.getPassword() != null && !usr.getPassword().isEmpty()) {
			loginUser.setPassword(passwordEncoder.encode(usr.getPassword()));
		}
		//loginUser.setRole(loginRolesService.findByName("ADMIN").get());
		Map<String, Boolean> ret = new HashMap<>();
		boolean status = loginUserService.saveLoginUser(loginUser) != null ? true : false;
		ret.put("successfull", status);
		return ret;

	}

}
