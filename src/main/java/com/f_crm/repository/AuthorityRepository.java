package com.f_crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.f_crm.entity.Authority;

public interface AuthorityRepository  extends JpaRepository<Authority, Integer>{

	@Query(nativeQuery = true, value = "select * from authorities where roleid = 'DOCTOR'")
	List<Authority> findByDoctor();

	
}
