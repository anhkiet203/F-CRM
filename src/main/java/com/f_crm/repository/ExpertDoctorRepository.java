package com.f_crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.f_crm.entity.ExpertDoctor;

public interface ExpertDoctorRepository extends JpaRepository<ExpertDoctor, Integer>{

	@Query("SELECT m FROM ExpertDoctor m WHERE m.active = true")
	List<ExpertDoctor> findActive();
	ExpertDoctor getByname(String name); 
}
