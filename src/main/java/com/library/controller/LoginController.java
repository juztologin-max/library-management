package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.dto.UserDTO;

@Controller
public class LoginController {
	
	@GetMapping("/")
	public String getRoot(Model m) {
		return "redirect:/login";

	}
	
	
	@GetMapping("/login")
	public String getLogin(Model m) {
		m.addAttribute("usr", new UserDTO());
		return "login";

	}
}
