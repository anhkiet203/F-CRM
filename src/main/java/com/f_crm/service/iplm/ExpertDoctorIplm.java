package com.f_crm.service.iplm;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f_crm.entity.ExpertDoctor;
import com.f_crm.repository.ExpertDoctorRepository;
import com.f_crm.service.ExpertDoctorService;


@Service
public class ExpertDoctorIplm implements ExpertDoctorService {

	@Autowired
	ExpertDoctorRepository dctRepo;

	@Override
	public List<ExpertDoctor> findActive() {

		return dctRepo.findActive();
	}

	@Override
	public ExpertDoctor findById(Integer id) {
		return dctRepo.findById(id).get();
	}

	@Override
	public ExpertDoctor create(ExpertDoctor expdtc) {
		return dctRepo.save(expdtc);

	}


	@Override
	public void delete(Integer id, boolean newStatus) {
		Optional<ExpertDoctor> methodOptional = dctRepo.findById(id);
		methodOptional.ifPresent(method -> {
			method.setActive(newStatus);
			dctRepo.save(method);
		});
	}

	@Override
	public List<ExpertDoctor> findAll() {
		dctRepo.findAll();
		return null;
	}

}
