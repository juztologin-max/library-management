package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	@GetMapping("/errors/access-denied")
	public String getAccessDenied() {
		return "/errors/access-denied";
	}
}
