package com.example.demo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.builder.DoctorBuilder;
import com.example.builder.PatientBuilder;
import com.example.controller.EMSController;
import com.example.model.Doctor;
import com.example.model.EMSEntity;
import com.example.model.EntityRequest;
import com.example.model.EntityResponse;
import com.example.model.Patient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EntityManagementApplication.class})
@SpringBootTest
public class EntityManagementApplicationTests {

	@Autowired
	private EMSController controller;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void testSystemHealth() {
		ResponseEntity healthCheck = controller.healthCheck();
		assertTrue(healthCheck.getStatusCode() == HttpStatus.OK);
	}
	
//	@Test
//    public void testCreatePatient() {
//        EMSEntity emsEntity = PatientBuilder.createPatientWithAddress().inMemory();
//        EntityRequest entityRequest = new EntityRequest("Patient", emsEntity);
//        ResponseEntity responseEntity = controller.createEntity(entityRequest);
//        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
//        assertTrue(((Patient)((EntityResponse)responseEntity.getBody()).getEntity()).getName().equals("Test_Name"));
//    }
//	
//	@Test
//    public void testUpdateAndGetPatient() {
//        String type = "Patient";
//        EMSEntity emsEntity = PatientBuilder.createPatient().inMemory();
//        EntityRequest entityRequest = new EntityRequest(type, emsEntity);
//        ResponseEntity responseEntity = controller.createEntity(entityRequest);
//        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
//        Patient patient = (Patient)((EntityResponse)responseEntity.getBody()).getEntity();
//        assertTrue(patient.getName().equals("Test_Name"));
//
//        patient.setName("Test_Name_2");
//        entityRequest.setEntity(patient);
//        responseEntity = controller.updateEntity(patient.getUuid(), entityRequest);
//        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
//
//        responseEntity = controller.getEntity(patient.getUuid(), type);
//        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
//        patient = (Patient)((EntityResponse)responseEntity.getBody()).getEntity();
//        assertTrue(patient.getName().equals("Test_Name_2"));
//    }
//	
//	
//	@Test
//	public void testDeletePatient() {
//		String type = "Patient";
//		EMSEntity emsEntity = PatientBuilder.createPatient().inMemory();
//		EntityRequest entityRequest = new EntityRequest(type, emsEntity);
//		ResponseEntity responseEntity = controller.createEntity(entityRequest);
//		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
//		Patient patient = (Patient) ((EntityResponse) responseEntity.getBody()).getEntity();
//		assertTrue(patient.getName().equals("Test_Name"));
//
//		entityRequest.setEntity(patient);
//
//		responseEntity = controller.deleteEntity(patient.getUuid(), entityRequest);
//		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
//
//	}
//	
//	
//	@Test
//    public void testCreateAndGetDoctorWithAddress() {
//        String typeDoctor = "Doctor";
//        EMSEntity emsDoctorEntity = DoctorBuilder.createDoctorWithAddress().inMemory();
//        EntityRequest entityDoctorRequest = new EntityRequest(typeDoctor, emsDoctorEntity);
//        ResponseEntity responseDoctorEntity = controller.createEntity(entityDoctorRequest);
//        assertTrue(responseDoctorEntity.getStatusCode() == HttpStatus.OK);
//        Doctor doctor = (Doctor)((EntityResponse)responseDoctorEntity.getBody()).getEntity();
//  
//	}
    
	@Test
    public void testCreateAndGetDoctorAndPatient() {
        String typeDoctor = "Doctor";
        EMSEntity emsDoctorEntity = DoctorBuilder.createDoctor().inMemory();
        EntityRequest entityDoctorRequest = new EntityRequest(typeDoctor, emsDoctorEntity);
        ResponseEntity responseDoctorEntity = controller.createEntity(entityDoctorRequest);
        assertTrue(responseDoctorEntity.getStatusCode() == HttpStatus.OK);
        Doctor doctor = (Doctor)((EntityResponse)responseDoctorEntity.getBody()).getEntity();
        assertTrue(doctor.getName().equals("Test_Name_Doc"));     
        
        String typePatient = "Patient";
       // EMSEntity emsPatientEntity = PatientBuilder.createPatient().withConsultingDoctorUuid(doctor.getUuid()).inMemory();
        
        /**
         * usually This is not requiredd, as using the ConsultingDoctorUuid, we can get the Doctor's detail from Doctor Collection
         */
        EMSEntity emsPatientEntity = PatientBuilder.createPatient().withConsultingDoctorUuid(doctor.getUuid()).withConsultingDoctor(doctor).inMemory();
        
        EntityRequest entityPatientRequest = new EntityRequest(typePatient, emsPatientEntity);
        ResponseEntity responsePatientEntity = controller.createEntity(entityPatientRequest);
        assertTrue(responsePatientEntity.getStatusCode() == HttpStatus.OK);
        Patient patient = (Patient)((EntityResponse)responsePatientEntity.getBody()).getEntity();
        assertTrue(patient.getName().equals("Test_Name"));

        responsePatientEntity = controller.getEntity(patient.getUuid(), typePatient);
        assertTrue(responsePatientEntity.getStatusCode() == HttpStatus.OK);
        patient = (Patient)((EntityResponse)responsePatientEntity.getBody()).getEntity();
        
        assertTrue(patient.getName().equals("Test_Name"));
        assertTrue(patient.getConsultingDoctor().getUuid().equals(doctor.getUuid()));
        assertTrue(patient.getConsultingDoctor().getName().equals(doctor.getName()));

       
        
        /**
         * There is no point of embedded patient again into
         *  Doctor as Patient already has the Doctor's full data.
         *  
         *  Doctor can just have List of Patient's uuids, 
         *  using which we can search the details of patient from 
         *  Patient collection.
         */
        
//        List<Patient> patients = new ArrayList<>();
//        patients.add(patient);
//        doctor.setPatients(patients);
//        entityDoctorRequest.setEntity(doctor);
//        responseDoctorEntity = controller.updateEntity(doctor.getUuid(), entityDoctorRequest);
//        assertTrue(responseDoctorEntity.getStatusCode() == HttpStatus.OK);
//        
//        responseDoctorEntity = controller.getEntity(doctor.getUuid(), typeDoctor);
        
        List<String> patientUuids = new ArrayList<>();
        patientUuids.add(patient.getUuid());
        doctor.setPatientUuid(patientUuids);
        entityDoctorRequest.setEntity(doctor);
        responseDoctorEntity = controller.updateEntity(doctor.getUuid(), entityDoctorRequest);
        assertTrue(responseDoctorEntity.getStatusCode() == HttpStatus.OK);
        
        responseDoctorEntity = controller.getEntity(doctor.getUuid(), typeDoctor);
        
         assertTrue(responseDoctorEntity.getStatusCode() == HttpStatus.OK);
        doctor = (Doctor)((EntityResponse)responseDoctorEntity.getBody()).getEntity();
        assertTrue(doctor.getName().equals("Test_Name_Doc"));
       // assertTrue(doctor.getPatients().get(0).getUuid().equals(patient.getUuid()));
        assertTrue(doctor.getPatientUuid().get(0).equals(patient.getUuid()));
    }	 
}
