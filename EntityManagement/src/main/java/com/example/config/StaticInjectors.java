package com.example.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.example.builder.DoctorBuilder;
import com.example.builder.PatientBuilder;
import com.example.repository.DoctorRepository;
import com.example.repository.PatientRepository;

@Configuration
public class StaticInjectors {

	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	DoctorRepository  doctorRepository;
	
	@PostConstruct
    public void inject() {
        PatientBuilder.setRepository(patientRepository);
        DoctorBuilder.setRepository(doctorRepository);
    }

}
