package com.f_crm.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.f_crm.entity.Users;
import com.f_crm.repository.UserRepository;

@Service
public class AuthorityService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users = userRepository.findById(username).get();
		System.out.println("username>> " + username);
		if(users != null && users.isActive() == true) {
			String password =users.getPassword() ;
			System.out.println("pass>> " + users.getPassword());
			System.out.println("pass>> " + password);
			String[] roles = users.getAuthorities().stream()
							.map(au -> au.getRole().getId())
							.collect(Collectors.toList()).toArray(new String[0]);
			System.out.println("role>> " + roles);
			return User.withUsername(username).password(password).roles(roles).build();
		} else {
			System.out.println("Không tồn tại tài khoản này");
			throw new UsernameNotFoundException(username + "not found!");
			
		}
	}
}
