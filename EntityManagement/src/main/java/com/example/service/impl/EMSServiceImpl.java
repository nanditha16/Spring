package com.example.service.impl;

import org.springframework.stereotype.Service;

import com.example.model.EMSEntity;
import com.example.model.EntityRequest;
import com.example.model.EntityResponse;
import com.example.service.EMSServiceInterface;
import com.example.utility.EntityHelper;

@Service
public class EMSServiceImpl implements EMSServiceInterface{


	@Override
	public EntityResponse createEntity(EntityRequest entityRequest) {
		String type = entityRequest.getType();
        EMSEntity entity = entityRequest.getEntity();
        return new EntityResponse(EntityHelper.createEntity(type, entity));
	}

	@Override
	public void updateEntity(String uuid, EntityRequest entityRequest) {
		String type = entityRequest.getType();
        EMSEntity entity = entityRequest.getEntity();
        EntityHelper.updateEntity(uuid, type, entity);
	}

	@Override
	public EntityResponse getEntity(String uuid, String type) {
		 return new EntityResponse(EntityHelper.getEntity(uuid, type));
	}

	@Override
	public void deleteEntity(String uuid, EntityRequest entityRequest) {
		String type = entityRequest.getType();
        EMSEntity entity = entityRequest.getEntity();
       
        EntityHelper.deleteEntity(uuid, type, entity);
	}

}
