package com.f_crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.f_crm.entity.Users;

public interface UserRepository extends JpaRepository<Users, String>{

	Users getByUsername (String username);
	Users getByEmail (String email);
	
	@Query("select o from Users o where o.active = 1")
	List<Users> findAlluser();
	
}
