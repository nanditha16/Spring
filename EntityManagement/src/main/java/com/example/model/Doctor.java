package com.example.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Entity
public class Doctor implements EMSEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5526943161542359194L;
	
	@Id
	private String id;
	private String name;
	private String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");;
	private LocalDateTime dateOfBirth;
	private Address address;
	
	private List<Patient> patients;
	
	private List<String> patientUuid;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<Patient> getPatients() {
		return patients;
	}

	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}
	
	public LocalDateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public List<String> getPatientUuid() {
		return patientUuid;
	}

	public void setPatientUuid(List<String> patientUuid) {
		this.patientUuid = patientUuid;
	}

	
	@Override
	public String toString() {
		return "Doctor{" +
	            "id=" + id +
	            ", name='" + name + '\'' +
	            ", uuid='" + uuid + '\'' +
	            ", Address= '" + address + '\'' +
	            ", PatientUuid= '" + patientUuid + '\'' +
	            '}';
	}
	
}
