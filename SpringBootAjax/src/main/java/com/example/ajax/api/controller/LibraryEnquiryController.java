package com.example.ajax.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ajax.api.model.Book;
import com.example.ajax.api.model.LibraryMember;
import com.example.ajax.api.model.ServiceResponse;
import com.example.ajax.api.service.LibraryService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

@RestController
public class LibraryEnquiryController {
	final static Logger logger = LoggerFactory.getLogger(LibraryEnquiryController.class);

	@Autowired
	private LibraryService libraryService;

	/*
	 * To save the new books in the library
	 */
	// Save book - post
	@PostMapping("/saveBook")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	public ResponseEntity<Object> addBook(@RequestBody Book book) {
		logger.info("Invoking saveBook endpoint... ");
		ServiceResponse<Book> response = new ServiceResponse<Book>("success", book);
		response.getData().setPurchasedDate(LocalDate.now());
		// response.getData().setBookStatus("Available");
		logger.info("Save Book details in bookCollection_db Collection... ");
		// use bookCollection_db - hard entry
		libraryService.addToLibrary(book);

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/*
	 * To save the new libraryMember registered to the library
	 */
	// Save book - post
	@PostMapping("/saveLibraryMember")
	public ResponseEntity<Object> savelibraryMember(@RequestBody LibraryMember libraryMember) {

		logger.info("Invoking savelibraryMember  endpoint... ");

		ServiceResponse<LibraryMember> response = new ServiceResponse<LibraryMember>("success", libraryMember);
		logger.info("Save libraryMember details in bookCollection_db Collection... ");
		// use bookCollection_db - hard entry
		libraryService.registerLibraryMember(libraryMember);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	// UPDATES

	/*
	 * update BookStatus with the "rented" by BookId and update the Library member
	 * with the book rented
	 */
	@PostMapping("/rentBookByBookId/{memberId}/{bookId}")
	public ResponseEntity<Object> rentBookByBookId(@PathVariable String memberId, @PathVariable int bookId) {
		logger.info("Invoking rentBookByBookId  endpoint... ");

		// get the Library Member by memberId to update the bookList he rents
		Optional<LibraryMember> libraryMember = libraryService.getLibraryMemberById(memberId);

		// get the Book by it's id and update the bookstatus
		Book updatedBook = libraryService.updateBookStatusByBookId(bookId);

		libraryService.updateLibraryMemberWithRentedBook(libraryMember, updatedBook);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Different GET ways to query for Books

	/*
	 * To get the list of all the books present in the library
	 */
	@GetMapping("/getBooks")
	// Retrieve data - get
	public ResponseEntity<Object> getAllBooks() {
		logger.info("Invoking getAllBooks endpoint... ");
		List<Book> books = libraryService.getAllBooks();

		ServiceResponse<List<Book>> response = new ServiceResponse<List<Book>>("success", books);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/*
	 * To get the list of books by bookId
	 */
	@GetMapping("/getBookByBookId/{bookId}")
	public ResponseEntity<Object> getBookByBookId(@PathVariable int bookId) {
		logger.info("Invoking getBookByBookId  endpoint... ");

		Book book = libraryService.getBookByBookId(bookId);
		ServiceResponse<Book> response = new ServiceResponse<Book>("success", book);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/*
	 * To get the list of all the books present and available for rent in the
	 * library
	 */
	@GetMapping("/getRentableBooks")
	// Retrieve data - get
	public ResponseEntity<Object> getRentableBooks() {
		logger.info("Invoking getRentableBooks endpoint... ");
		Optional<List<Book>> books = libraryService.rentableBooks();

		ServiceResponse<Optional<List<Book>>> response = new ServiceResponse<Optional<List<Book>>>("success", books);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/*
	 * To get the list of all the books present in the library by author
	 */
	@GetMapping("/getBooksByAuthor/{bookAuthor}")
	public ResponseEntity<Object> getBooksByAuthor(@PathVariable String bookAuthor) {
		logger.info("Invoking getBooksByAuthor  endpoint... ");

		Optional<List<Book>> books = libraryService.getBooksByAuthor(bookAuthor);
		ServiceResponse<Optional<List<Book>>> response = new ServiceResponse<Optional<List<Book>>>("success", books);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	// Different GET ways to query for getting Library Members

	/*
	 * To get the list of library member by city
	 */
	@GetMapping("/getLibraryMembersByAddress/{city}")
	public ResponseEntity<Object> getLibraryMembersByAddress(@PathVariable String city) {
		logger.info("Invoking getLibraryMembersByAddress  endpoint... ");

		List<LibraryMember> libraryMembers = libraryService.getLibraryMembersByCity(city);
		ServiceResponse<List<LibraryMember>> response = new ServiceResponse<List<LibraryMember>>("success",
				libraryMembers);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/*
	 * To get the library member details by their Name
	 */
	@GetMapping("/getLibraryMemberByName/{name}")
	// Retrieve data - get
	public ResponseEntity<Object> getLibraryMemberByName(@PathVariable String name) {
		logger.info("Invoking getLibraryMemberByName  endpoint... ");

		Optional<LibraryMember> libraryMemberDetails = libraryService.getLibraryMemberByName(name);

		ServiceResponse<Optional<LibraryMember>> response = new ServiceResponse<Optional<LibraryMember>>("success",
				libraryMemberDetails);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/*
	 * To get the library member details by their memberId - unique
	 */
	@GetMapping("/getLibraryMemberById/{memberId}")
	// Retrieve data - get
	public ResponseEntity<Object> getLibraryMemberById(@PathVariable String memberId) {
		logger.info("Invoking getLibraryMemberById  endpoint... ");

		Optional<LibraryMember> libraryMemberDetails = libraryService.getLibraryMemberById(memberId);

		ServiceResponse<Optional<LibraryMember>> response = new ServiceResponse<Optional<LibraryMember>>("success",
				libraryMemberDetails);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

}
