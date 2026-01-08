package com.library.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.dto.UserDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    @Valid
    public String getAdminDashboard(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Dashboard");
        m.addAttribute("mainMenuItem", "Dashboard");
        m.addAttribute("content", "admin/dashboard :: content");
        return "admin/admin-layout";
    }

    @GetMapping("/manage-admin")
    @Valid
    public String getManageAdmin(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Settings :: Manage Admin");
        m.addAttribute("mainMenuItem", "Settings");
        m.addAttribute("subMenuItem", "Manage Admin");
        m.addAttribute("content", "admin/manage-admin :: content");
        return "admin/admin-layout";
    }

    @GetMapping("/manage-librarian")
    @Valid
    public String getManageLibrarians(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Manage Librarians");
        m.addAttribute("mainMenuItem", "Manage Librarians");
        m.addAttribute("content", "admin/manage-librarian :: content");
        return "admin/admin-layout";
    }
    
    @GetMapping("/manage-user")
    @Valid
    public String getManageUsers(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Manage Users");
        m.addAttribute("mainMenuItem", "Manage Users");
        m.addAttribute("content", "admin/manage-user :: content");
        return "admin/admin-layout";
    }
    
    @GetMapping("/manage-book")
    @Valid
    public String getManageBook(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Manage Books");
        m.addAttribute("mainMenuItem", "Manage Books");
        m.addAttribute("content", "admin/manage-book :: content");
        return "admin/admin-layout";
    }
}
