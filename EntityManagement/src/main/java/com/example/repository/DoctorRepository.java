package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Doctor;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String>, EMSRepository<Doctor>{
		
}
