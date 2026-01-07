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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Librarian;
import com.library.entity.LoginUser;
import com.library.entity.LoginUserDetails;
import com.library.entity.User;
import com.library.service.LoginRolesService;
import com.library.service.LoginUserService;
import com.library.service.UserService;

import jakarta.validation.Valid;
import tools.jackson.databind.JsonNode;

@RequestMapping("/admin/api/manage-user")
@RestController
public class ManageUserController {
	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginRolesService loginRolesService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/save")
	public Map<String, Boolean> saveUser(@RequestBody JsonNode jsonNode, @AuthenticationPrincipal UserDetails usr) {
		LoginUser loginUser = new LoginUser();

		loginUser.setEnabled(jsonNode.get("enabled").asBoolean());
		loginUser.setName(jsonNode.get("name").asString());
		loginUser.setPassword(passwordEncoder.encode(jsonNode.get("password").asString()));
		loginUser.setRole(loginRolesService.findByName("LIBRARIAN").get());
		Map<String, Boolean> ret = new HashMap<>();
		ret.put("successfull", false);

		loginUser = loginUserService.saveLoginUser(loginUser);
		if (loginUser.getId() != null) {
			User user = new User();
			user.setLegalName(jsonNode.get("legalName").asString());
			user.setLoginUser(loginUser);
			user.setAddress(jsonNode.get("address").asString());
			user.setEmail(jsonNode.get("email").asString());
			user.setPhoneNo(jsonNode.get("phone").asString());
			user.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
			user.setCreatedBy(((LoginUserDetails) usr).getUser());
			user.setUpdatedBy(((LoginUserDetails) usr).getUser());
			try {
				user = userService.saveUser(user);
			} catch (Exception ex) {
				ret.put("successfull", false);
			}
			ret.put("successfull", user.getId() != null);
		} else {
			loginUserService.deleteLoginUser(loginUser);
		}
		return ret;
	}

	@PostMapping("/list")
	public PagedModel<User> getListOfUsers(@RequestBody JsonNode payload) {
		return new PagedModel<>(userService.listAll(payload));

	}

	@PostMapping("/search")
	public PagedModel<User> getListOfUsersMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(userService.findAll(payload));

	}

	@PutMapping("/{id}")
	@Valid
	public Map<String, Boolean> updateUser(@Valid @PathVariable Long id, @RequestBody JsonNode jsonNode,
			@AuthenticationPrincipal UserDetails usr) {
		User user = userService.findById(id).get();
		Map<String, Boolean> ret = new HashMap<>();
		ret.put("successfull", false);
		if (user != null) {
			user.getLoginUser().setEnabled(jsonNode.get("enabled").asBoolean());
			user.getLoginUser().setName(jsonNode.get("name").asString());
			if (!jsonNode.get("password").asString().isBlank()) {
				user.getLoginUser().setPassword(passwordEncoder.encode(jsonNode.get("password").asString()));
			}

			LoginUser loginUser = loginUserService.saveLoginUser(user.getLoginUser());
			user.setLegalName(jsonNode.get("legalName").asString());
			user.setLoginUser(loginUser);
			user.setAddress(jsonNode.get("address").asString());
			user.setEmail(jsonNode.get("email").asString());
			user.setPhoneNo(jsonNode.get("phone").asString());
			user.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

			user.setUpdatedBy(((LoginUserDetails) usr).getUser());
			try {
				user = userService.saveUser(user);
			} catch (Exception ex) {
				ret.put("successfull", false);
			}
			ret.put("successfull", user.getId() != null);
		}
		return ret;

	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable Long id) {
		boolean status = true;
		User user = new User();
		user.setId(id);
		Map<String, Boolean> ret = new HashMap<>();
		try {
			userService.deleteUser(user);
		} catch (Exception ex) {
			status = false;
		}
		ret.put("successfull", status);
		return ret;

	}

}
