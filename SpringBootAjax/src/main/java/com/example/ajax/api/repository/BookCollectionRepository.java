package com.example.ajax.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.ajax.api.model.Book;

public interface BookCollectionRepository extends MongoRepository<Book, String>{
	
	@Query("{'bookName': ?0}")
	Optional<Book> findByGameName(final String bookName);

	@Query("{'bookAuthor': ?0}")
	Optional<List<Book>> findByBookAuthor(final String bookAuthor);

}
