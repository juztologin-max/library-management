package com.library.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

@Controller
@PreAuthorize("hasAuthority('ADMIN')||hasAuthority('LIBRARIAN')||hasAuthority('USER')")
public class DashboardController {

	@GetMapping("/dashboard")
	@Valid
	public String getAdminDashboard(@AuthenticationPrincipal UserDetails usr) throws Exception {
		String ret = "";
		for (GrantedAuthority authority : usr.getAuthorities()) {
			switch (authority.toString()) {
				case "ADMIN" -> {
					ret = "redirect:/admin/dashboard";
				}
				case "USER" -> {
					ret = "redirect:/user/dashboard";
				}
				case "LIBRARIAN" -> {
					ret = "redirect:/librarian/dashboard";
				}
				default -> throw new Exception("Unknown User type: " + authority.toString());
			}

		}
		return ret;
	}

}
