package com.example.ajax.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.ajax.api.model.LibraryMember;

public interface LibraryMemberEnquiryRepository extends MongoRepository<LibraryMember, String> {

	/*
	 * To get the list of library member details by their Name
	 */
	@Query("{'memberName': ?0}")
	Optional<LibraryMember> findByMemberName(final String memberName);

	/*
	 * To get the list of library member by their city
	 */
	@Query("{'Address.city':?0}")
	public List<LibraryMember> findByCity(String city);

}
