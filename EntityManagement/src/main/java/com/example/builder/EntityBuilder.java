package com.example.builder;

import com.example.model.EMSEntity;
import com.example.repository.EMSRepository;

public interface EntityBuilder {

	 public EMSEntity saveEntity(EMSEntity emsEntity);
	 public EMSEntity inSaveDatabase();
	 public EMSRepository getRepository();
	    
	 public void deleteEntity(EMSEntity emsEntity);
	 public void inDeleteDatabase();
}
