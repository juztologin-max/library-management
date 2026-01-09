package com.library.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.dto.UserDTO;

import jakarta.validation.Valid;

@Controller

public class DashboardController {

	@GetMapping("/dashboard")
	@Valid
	public String getAdminDashboard(@AuthenticationPrincipal UserDetails usr) throws Exception {
		String ret = "";
		for (GrantedAuthority authority : usr.getAuthorities()) {
			if (authority.toString().equals("ADMIN")) {
				ret = "redirect:/admin/dashboard";
				break;
			} else if (authority.toString().equals("USER")) {
				ret = "redirect:/user/dashboard";
				break;
			} else if (authority.toString().equals("LIBRARIAN")) {
				ret = "redirect:/librarian/dashboard";
				break;
			}

			else {
				throw new Exception("Unknown User type: " + authority.toString());
			}

		}
		return ret;
	}

}
