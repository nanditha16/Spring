package com.example.builder;

import java.util.List;

import com.example.model.Address;
import com.example.model.Doctor;
import com.example.model.EMSEntity;
import com.example.model.Patient;
import com.example.repository.DoctorRepository;

public class DoctorBuilder implements EntityBuilder {

	private static DoctorRepository doctorRepository;
    private Doctor doctor = new Doctor();
	
    public static DoctorBuilder createDoctor() {
        return new DoctorBuilder().withName("Test_Name_Doc");
    }
    
    public static DoctorBuilder createDoctorWithAddress() {
        return new DoctorBuilder().withName("Test_Name_Doc").withAddress(new Address("19 Imaginary Road", "Imaginary Place", "Imaginary City", "UK"));
    }
    
    public DoctorBuilder withPatients(List<Patient> patients) {
    	doctor.setPatients(patients);
    	return this;
    }
    
    public DoctorBuilder withAddress(Address address) {
        doctor.setAddress(address);
        return this;
    }
    
    public DoctorBuilder withId(String id) {
        doctor.setId(id);
        return this;
    }
	
    public DoctorBuilder withName(String name) {
        doctor.setName(name);
        return this;
    }

    public DoctorBuilder withUuid(String uuid) {
        doctor.setUuid(uuid);
        return this;
    }

    public DoctorBuilder withPatientUuid( List<String> patientUuid) {
    	doctor.setPatientUuid(patientUuid);
    	return this;
    }
   
    public Doctor inMemory() {
        return doctor;
    }
    
    public static void setRepository(DoctorRepository doctorRepository) {
        DoctorBuilder.doctorRepository = doctorRepository;
    }
	
    public DoctorRepository getRepository() {
		return doctorRepository;
	}
    
	@Override
	public EMSEntity saveEntity(EMSEntity emsEntity) {
		Doctor existingDoctor = (Doctor)emsEntity;
        return new DoctorBuilder().withId(existingDoctor.getId()).withPatientUuid(existingDoctor.getPatientUuid()).withPatients(existingDoctor.getPatients()).withName(existingDoctor.getName()).withAddress(existingDoctor.getAddress()).withUuid(existingDoctor.getUuid()).inSaveDatabase();
	}

	@Override
	public EMSEntity inSaveDatabase() {
		return doctorRepository.save(doctor);
	}

	@Override
	public void deleteEntity(EMSEntity emsEntity) {
		Doctor existingDoctor = (Doctor)emsEntity;
        new DoctorBuilder().withId(existingDoctor.getId()).withPatientUuid(existingDoctor.getPatientUuid()).withName(existingDoctor.getName()).withPatients(existingDoctor.getPatients()).withAddress(existingDoctor.getAddress()).withUuid(existingDoctor.getUuid()).inDeleteDatabase();
	
	}

	@Override
	public void inDeleteDatabase() {
		doctorRepository.deleteById(doctor.getId());
	}


	
}
