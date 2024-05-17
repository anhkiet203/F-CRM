package com.f_crm.Usercontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String Home() {
		return "User/home/index";
	}

	@GetMapping("/signin")
	public String Signin() {
		return "User/layout/signin";

	}

	@GetMapping("/forgot")
	public String Forgot() {
		return "User/layout/forgot_password";
	}
	
}
