package com.f_crm.service.iplm;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.f_crm.entity.Users;
import com.f_crm.repository.UserRepository;
import com.f_crm.service.UserService;

@Service
public class UserIplm implements UserService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder pe;
	
	
	@Override
	public List<Users> findAll() {
		
		return userRepository.findAll();
	}

	@Override
	public Users getByUsername(String username) {
		return userRepository.findById(username).get();
	}



	@Override
	public Users getByEmail(String email) {
		
		return userRepository.findById(email).get();
	}

	@Override
	public void delete(String username) {
		Users user = userRepository.getByUsername(username);
		user.setActive(false);
		userRepository.save(user);
	}

	@Override
	public Users update(Users user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<Users> findById(String username) {
		return userRepository.findById(username);
	}


	@Override
	public void changePass(Users users, String newPassword) {
		String encodedPassword = pe.encode(newPassword);
        users.setPassword(encodedPassword);
	}


}
