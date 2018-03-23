package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.model.Patient;

public interface PatientRepository extends MongoRepository<Patient, String>, EMSRepository<Patient>{

}
