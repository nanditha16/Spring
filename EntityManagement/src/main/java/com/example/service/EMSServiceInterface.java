package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.EntityRequest;
import com.example.model.EntityResponse;

@Service
public interface EMSServiceInterface {
	public EntityResponse createEntity(EntityRequest entity);
    public void updateEntity(String uuid, EntityRequest entity);
    public EntityResponse getEntity(String uuid, String type);
    public void deleteEntity(String uuid, EntityRequest entity);
    
	
}
