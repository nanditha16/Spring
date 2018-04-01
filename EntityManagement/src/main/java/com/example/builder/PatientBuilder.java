package com.example.builder;

import java.time.LocalDate;

import com.example.model.Address;
import com.example.model.Doctor;
import com.example.model.EMSEntity;
import com.example.model.Patient;
import com.example.repository.PatientRepository;

public class PatientBuilder implements EntityBuilder {

	private static PatientRepository patientRepository;
    private Patient patient = new Patient();
    
    public static PatientBuilder createPatient() {
        return new PatientBuilder().withName("Test_Name");
    }
    
    public static PatientBuilder createPatientWithAddress() {
        return new PatientBuilder().withName("Test_Name").withAddress(new Address("19 Imaginary Road", "Imaginary Place", "Imaginary City", "US"));
    }
    
    public PatientBuilder withAddress(Address address) {
    	patient.setAddress(address);
        return this;
    }
    
    public PatientBuilder withDateOfBirth(LocalDate dateOfBirth) {
    	patient.setDateOfBirth(dateOfBirth);
        return this;
    }
    
    public PatientBuilder withId(String id) {
        patient.setId(id);
        return this;
    }
	
    public PatientBuilder withName(String name) {
        patient.setName(name);
        return this;
    }

    public PatientBuilder withUuid(String uuid) {
        patient.setUuid(uuid);
        return this;
    }

    public PatientBuilder withConsultingDoctorUuid(String uuid) {
        patient.setConsultingDoctorUuid(uuid);
        return this;
    }
    
    public PatientBuilder withConsultingDoctor(Doctor consultingDoctor) {
        patient.setConsultingDoctor(consultingDoctor);
        return this;
    }

    public Patient inMemory() {
        return patient;
    }
    
    public static void setRepository(PatientRepository patientRepository) {
        PatientBuilder.patientRepository = patientRepository;
    }
    
    public PatientRepository getRepository() {
		 return patientRepository;
	}
    
	@Override
	public EMSEntity saveEntity(EMSEntity emsEntity) {
		 Patient existingPatient = (Patient)emsEntity;
		 return new PatientBuilder().withId(existingPatient.getId()).withName(existingPatient.getName())
	            .withUuid(existingPatient.getUuid()).withDateOfBirth(existingPatient.getDateOfBirth()).withAddress(existingPatient.getAddress()).withConsultingDoctor(existingPatient.getConsultingDoctor()).withConsultingDoctorUuid(existingPatient.getConsultingDoctorUuid()).inSaveDatabase();
	}

	@Override
	public EMSEntity inSaveDatabase() {
		return patientRepository.save(patient);
	}


	@Override
	public void deleteEntity(EMSEntity emsEntity) {
		 Patient existingPatient = (Patient)emsEntity;
		  new PatientBuilder().withId(existingPatient.getId()).withName(existingPatient.getName())
	            .withUuid(existingPatient.getUuid()).withDateOfBirth(existingPatient.getDateOfBirth()).withAddress(existingPatient.getAddress()).withConsultingDoctor(existingPatient.getConsultingDoctor()).withConsultingDoctorUuid(existingPatient.getConsultingDoctorUuid()).inDeleteDatabase();

	}

	@Override
	public void inDeleteDatabase() {
		patientRepository.deleteById(patient.getId());
		
	}


}
