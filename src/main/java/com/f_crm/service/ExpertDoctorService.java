package com.f_crm.service;

import java.util.List;
import com.f_crm.entity.ExpertDoctor;




public interface ExpertDoctorService {

List<ExpertDoctor> findAll();
	
	public ExpertDoctor findById(Integer id);
	
	public ExpertDoctor create(ExpertDoctor expertDoctor);
	

	public void delete(Integer id, boolean newStatus);

	List<ExpertDoctor> findActive();

}

