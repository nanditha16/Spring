package com.example.ajax.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ajax.api.model.Book;
import com.example.ajax.api.model.ServiceResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

@RestController
public class BookController {
	
	List<Book> bookStore = new ArrayList<>();
	
	// Save book - post
	@PostMapping("/saveBook")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	public ResponseEntity<Object> addBook(@RequestBody Book book){
		bookStore.add(book);
		ServiceResponse<Book> response = new ServiceResponse<Book>("success",book );
		response.getData().setPurchasedDate(LocalDate.now());
		return new ResponseEntity<Object>(response, HttpStatus.OK);		
	}
	
	@GetMapping("/getBooks")
	// Retrieve data - get
	public ResponseEntity<Object> getAllBooks(){
		ServiceResponse<List<Book>> response = new ServiceResponse<List<Book>>("success",bookStore );
		return new ResponseEntity<Object>(response, HttpStatus.OK);		
	}
}
