package com.library.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.dto.UserDTO;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("/dashboard")
	public String getAdminDashboard(Model m, @AuthenticationPrincipal UserDetails usr) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(usr.getUsername());
		m.addAttribute("usr", userDTO);
		m.addAttribute("title", "Dashboard");
		m.addAttribute("content", "admin/dashboard :: content");
		return "admin/admin-layout";
	}

	@GetMapping("/manage-admin")
	public String getManageAdmin(Model m, @AuthenticationPrincipal UserDetails usr) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(usr.getUsername());
		m.addAttribute("usr", userDTO);
		m.addAttribute("title", "Settings :: Manage Admin");
		m.addAttribute("content", "admin/manage-admin :: content");
		return "admin/admin-layout";
	}
}
