package com.library.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.LoginUserDetails;
import com.library.projections.librarian.LibrarianDuesProjection;
import com.library.service.admin.AdminUserService;
import com.library.service.user.UserDuesService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/user/api/manage-dues")
@RestController
public class UserDuesController {
	@Autowired
	private UserDuesService UserDuesService;

	@Autowired
	private AdminUserService userService;

	@PostMapping("/list-dues")
	public PagedModel<LibrarianDuesProjection> listBorrowings(@RequestBody JsonNode payload,@AuthenticationPrincipal UserDetails usr) {
		
		return new PagedModel<>(UserDuesService.listAll(payload,userService.findByLoginUser(((LoginUserDetails)usr).getUser()).get().getId()));

	}
	

	}
