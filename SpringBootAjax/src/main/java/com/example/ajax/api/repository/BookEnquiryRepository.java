package com.example.ajax.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.ajax.api.model.Book;

public interface BookEnquiryRepository extends MongoRepository<Book, String>{
	
	/*
	 * To get the book detail by it's name
	 */
	@Query("{'bookName': ?0}")
	Optional<Book> findByGameName(final String bookName);

	/*
	 *  To get the list of books by Author
	 */
	@Query("{'bookAuthor': ?0}")
	Optional<List<Book>> findByBookAuthor(final String bookAuthor);

	/*
	 *  To get the list of books by bookStatus
	 */
	@Query("{ 'bookStatus' : { $exists: ?0 } }")
	Optional<List<Book>> findByBookStatus(Boolean string);

	/*
	 * To get the book detail by it's bookId
	 */
	@Query("{'bookId': ?0}")
	Book findByBookId(int bookId);

	
}
