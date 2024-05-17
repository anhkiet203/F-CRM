package com.f_crm.service;

import java.util.List;
import java.util.Optional;

import com.f_crm.entity.Users;

public interface UserService {

	List<Users> findAll();
	Users getByUsername (String username);
	Users getByEmail (String email);
	Users update(Users user);
	void delete(String username);
	Optional<Users> findById(String username);
	void changePass(Users users, String newPassword);
	
}
