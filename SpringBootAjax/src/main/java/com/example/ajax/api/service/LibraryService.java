package com.example.ajax.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ajax.api.model.Book;
import com.example.ajax.api.repository.BookCollectionRepository;


@Service
public class LibraryService {

	final static Logger logger = LoggerFactory.getLogger(LibraryService.class);

	@Autowired
	private BookCollectionRepository bookCollectionRepository;
	
	public Book addToLibrary(Book book) {
		logger.info("Add document to Mongo Collection - library ");
		book = bookCollectionRepository.save(book);
		return book;
	}
	
}
