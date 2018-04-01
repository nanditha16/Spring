package com.example.model;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;

public class Patient implements EMSEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5524997432442793590L;
	
	@Id
	private String id;
	private String name;
	private String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	private LocalDate dateOfBirth;
	private Address address;
	
	 private String consultingDoctorUuid;
	 
	 private Doctor consultingDoctor;
	 
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

	public String getConsultingDoctorUuid() {
		return consultingDoctorUuid;
	}

	public void setConsultingDoctorUuid(String consultingDoctorUuid) {
		this.consultingDoctorUuid = consultingDoctorUuid;
	}

	public Doctor getConsultingDoctor() {
		return consultingDoctor;
	}

	public void setConsultingDoctor(Doctor consultingDoctor) {
		this.consultingDoctor = consultingDoctor;
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	
	@Override
	public String toString() {
		 return "Patient{" +
		            "id=" + id +
		            ", name='" + name + '\'' +
		            ", uuid='" + uuid + '\'' +
		            ", DateOfBirth='" + dateOfBirth + '\'' +
		            ", consultingDoctorUuid='" + consultingDoctorUuid + '\'' +
		            ", Address= '" + address + '\'' +
		            '}';
	}
}
