package com.f_crm.service;

import java.util.List;

import com.f_crm.entity.Method;



public interface MethodService {
	
	public Method findById(Integer id);
	
	public Method create(Method method);
	
	public Method update(Method method);
	
	public void delete(Integer id, boolean newStatus);

	List<Method> findActive();

	List<Method> findAll();
}