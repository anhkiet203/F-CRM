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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.f_crm.entity.ExpertDoctor;
import com.f_crm.repository.ExpertDoctorRepository;
import com.f_crm.service.ExpertDoctorService;

@Controller
public class ExpertController {

	@Autowired ExpertDoctorRepository expertDoctorRep;
	@Autowired
	ExpertDoctorService expdtcService;

	@GetMapping("/doctor")
	public String doctorList(@ModelAttribute("expdtc")ExpertDoctor expdtc, Model model) {
		List<ExpertDoctor> expdtc1 = expdtcService.findActive();
		model.addAttribute("expdtc", expdtc1);
		return "Admin/expertDoctor/list";
	}

	@GetMapping("/doctorform")
	public String doctorForm(@ModelAttribute("expdtc")ExpertDoctor expdtc, Model model) {
		 return "Admin/expertDoctor/form";
	}


	@PostMapping("/adddoctor")
	public String addDoctor(@Valid @ModelAttribute("expdtc")ExpertDoctor expdtc, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "Admin/expertDoctor/form";
	    }
	    if(expertDoctorRep.getByname(expdtc.getName())!=null) {
			model.addAttribute("messagerror", "Tên chuyên ngành đã bị trùng !");
			return "Admin/expertDoctor/form";
		}
	    expdtcService.create(expdtc);
	    model.addAttribute("successMessage", "Create Success!");
	    return "Admin/expertDoctor/form";
	}
	@PostMapping("/updoctor")
	public String upDoctor(@Valid @ModelAttribute("expdtc")ExpertDoctor expdtc, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "Admin/expertDoctor/formupdate";
	    }
	   
	    expdtcService.create(expdtc);
	    model.addAttribute("successMessagess", "Update Success!");
	    return "Admin/expertDoctor/formupdate";
	}

	

	@GetMapping("/doctorform/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
	    ExpertDoctor expdtc = expdtcService.findById(id);
	    model.addAttribute("expdtc", expdtc);
	    return "Admin/expertDoctor/formupdate";
	}

	@GetMapping("/changeStatusdoc/{id}")
	public String changeStatus(@PathVariable("id") int id, Model model) {
		expdtcService.delete(id, false);
		model.addAttribute("messageChange", "Change Success!");
		return "redirect:/doctor";
	}

}
