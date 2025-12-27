package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginLogoutController {

	@GetMapping("/")
	public String getRoot() {
		return "redirect:/login";

	}

	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}


}
