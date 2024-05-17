package com.f_crm.service.iplm;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f_crm.entity.Method;
import com.f_crm.repository.MethodRepository;
import com.f_crm.service.MethodService;

@Service
public class MethodIplm implements MethodService{

	@Autowired
	MethodRepository methodRepo;
	
	@Override
	public List<Method> findActive() {
	    return methodRepo.findActive();
	}

	@Override
	public Method findById(Integer id) {
		return methodRepo.findById(id).get();
	}

	@Override
	public Method create(Method method) {
		return methodRepo.save(method);
	}

	@Override
	public Method update(Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id, boolean newStatus) {
	    Optional<Method> methodOptional = methodRepo.findById(id);
	    methodOptional.ifPresent(method -> {
	        method.setActive(newStatus);
	        methodRepo.save(method); // Lưu thay đổi vào cơ sở dữ liệu
	    });
	}

	@Override
	public List<Method> findAll() {
		return methodRepo.findAll();
	}

	
}
