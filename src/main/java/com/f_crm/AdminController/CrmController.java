package com.f_crm.AdminController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.f_crm.entity.Crm_entry;
import com.f_crm.entity.Customer;
import com.f_crm.entity.ExpertDoctor;
import com.f_crm.entity.Method;
import com.f_crm.entity.Users;
import com.f_crm.repository.Crm_entryRepository;
import com.f_crm.repository.CustomerRepository;
import com.f_crm.repository.ExpertDoctorRepository;
import com.f_crm.repository.MethodRepository;
import com.f_crm.repository.UserRepository;

@Controller
public class CrmController {

	@Autowired
	CustomerRepository customerRepository;

	@GetMapping("/crmentry")
	public String Crmlist(Model model) {
		
		List<Customer> customer = customerRepository.findAll();
		
		model.addAttribute("crms", customer);
		return "Admin/crm_entry/list";
	}
}
