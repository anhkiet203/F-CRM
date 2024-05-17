package com.f_crm.AdminController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.f_crm.entity.Role;
import com.f_crm.repository.RoleRepository;

@Controller
public class RoleController {

	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/role")
	public String showRole(Model model) {
		List<Role> role = roleRepository.findAll();
		model.addAttribute("roles", role);
		return "Admin/role/list";
	}
}
