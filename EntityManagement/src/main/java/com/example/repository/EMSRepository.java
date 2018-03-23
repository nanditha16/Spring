package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

public interface EMSRepository<T> {
	T findByUuid(String uuid);
	
	@Query("{'address.country': ?0}")
	  List<T> findByCountry(final String country);
	
}
