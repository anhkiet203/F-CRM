package com.f_crm.AdminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/authorities")
	public String Autho() {
		return "Admin/authorities/list";
	}
	
}
