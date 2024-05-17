package com.f_crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.f_crm.entity.Method;

public interface MethodRepository extends JpaRepository<Method, Integer>{

    @Query("SELECT m FROM Method m WHERE m.active = true")
	List<Method> findActive();

	Method getByname(String name); 

}
