package com.f_crm.AdminController;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.f_crm.entity.Method;
import com.f_crm.repository.MethodRepository;
import com.f_crm.service.MethodService;

@Controller

public class MethodController {

	@Autowired
	MethodRepository methodRepository;
	@Autowired
	MethodService methService;

	@GetMapping("/method")
	public String methodlist(@ModelAttribute("method") Method method, Model model) {
		List<Method> method1 = methService.findActive();
		model.addAttribute("method", method1);
		return "Admin/method/list";
	}

	@GetMapping("/methodform")
	public String methodform(@ModelAttribute("method") Method method, Model model) {
		return "Admin/method/form";
	}

	@PostMapping("/addmethod")
	public String addMethod(@Valid @ModelAttribute("method") Method method, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "Admin/method/form";
		}
		if (methodRepository.getByname(method.getName()) != null) {
			model.addAttribute("messagerror", "Tên phương pháp đã bị trùng !");
			return "Admin/method/form";
		}
		methService.create(method);
		model.addAttribute("messageAdd", "Create Success!");
		return "Admin/method/form";
	}

	@PostMapping("/upmethod")
	public String upMethod(@Valid @ModelAttribute("method") Method method, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "Admin/method/formupdate";
		}
		methService.create(method);
		model.addAttribute("messagess", "Create Success!");
		return "Admin/method/formupdate";
	}

	@GetMapping("/methodform/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Method method = methService.findById(id);
		model.addAttribute("method", method);
		return "Admin/method/formupdate";
	}

	@GetMapping("/changeStatus/{id}")
	public String changeStatus(@PathVariable("id") int id, Model model) {
		methService.delete(id, false);
		model.addAttribute("messageChange", "Change Success!");
		return "redirect:/method";
	}

}
