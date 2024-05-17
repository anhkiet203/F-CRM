package com.f_crm.restController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.f_crm.entity.Authority;
import com.f_crm.repository.AuthorityRepository;
import com.f_crm.repository.RoleRepository;
import com.f_crm.repository.UserRepository;

@CrossOrigin("*")
@RestController
public class AuthorityRestController {

	@Autowired UserRepository userRepository;
	@Autowired RoleRepository roleRepository;
	@Autowired AuthorityRepository authorityRepository;
	
	@GetMapping("/rest/authorities")
	public Map<String, Object> getAuthorities(){
		Map<String, Object> data = new HashMap<>();
		data.put("authorities", authorityRepository.findAll());
		data.put("roles", roleRepository.findAll());
		data.put("users", userRepository.findAlluser());
		return data;
	}
	
	@GetMapping("/api/user")
	public Principal getUser(Authentication authentication) {
		return authentication;
	}
	
	@PostMapping("/rest/authorities")
	public Authority create(@RequestBody Authority userRole){
		return authorityRepository.save(userRole);
	}
	
	@DeleteMapping("/rest/authorities/{id}")
	public void delete(@PathVariable("id") Integer id) {
		authorityRepository.deleteById(id);
	}
}
