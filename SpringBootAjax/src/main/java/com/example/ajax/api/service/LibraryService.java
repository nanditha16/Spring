package com.example.ajax.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ajax.api.model.Book;
import com.example.ajax.api.model.LibraryMember;
import com.example.ajax.api.repository.BookEnquiryRepository;
import com.example.ajax.api.repository.LibraryMemberEnquiryRepository;

@Service
public class LibraryService {

	final static Logger logger = LoggerFactory.getLogger(LibraryService.class);

	@Autowired
	private BookEnquiryRepository bookCollectionRepository;

	@Autowired
	private LibraryMemberEnquiryRepository libraryMemberEnquiryRepository;

	/*
	 * Save the book details in the library - in mongo
	 */
	public Book addToLibrary(Book book) {
		logger.info("Add document to Mongo Collection - library ");
		book = bookCollectionRepository.save(book);
		return book;
	}

	/*
	 * Register the library member : unique memberName
	 */
	public LibraryMember registerLibraryMember(LibraryMember libraryMember) {
		logger.info("Add document to Mongo Collection - libraryMember ");
		libraryMember = libraryMemberEnquiryRepository.save(libraryMember);
		return libraryMember;
	}

	// update book fields
	public Book updateBookStatusByBookId(int bookId) {
		logger.info("update BookStatus By BookId from Mongo Collection - library ");
		Book book = getBookByBookId(bookId);
		book.setBookStatus("Rented");
		book = bookCollectionRepository.save(book);
		return book;
	}

	// update LibraryMember With RentedBook By BookId
	public LibraryMember updateLibraryMemberWithRentedBook(Optional<LibraryMember> optionalLibraryMember, Book book) {
		logger.info("update LibraryMember With RentedBook By BookId from Mongo Collection - libraryMember ");

		if (optionalLibraryMember.isPresent()) {
			LibraryMember libraryMember = optionalLibraryMember.get();
			List<Book> booksRented = new ArrayList<Book>();
			booksRented = libraryMember.getBooks();
			if ((null == libraryMember.getBooks())) {
				booksRented = new ArrayList<Book>();
			} else {
				booksRented = libraryMember.getBooks();
			}
			booksRented.add(book);
			libraryMember.setBooks(booksRented);
			libraryMemberEnquiryRepository.save(libraryMember);
		}
		return optionalLibraryMember.get();
	}

	// Get books

	public List<Book> getAllBooks() {
		logger.info("Get all books document from Mongo Collection - library ");
		return bookCollectionRepository.findAll();
	}

	public Book getBookByBookId(int bookId) {
		logger.info("Get book document by BookId from Mongo Collection - library ");
		return bookCollectionRepository.findByBookId(bookId);
	}

	public Optional<List<Book>> rentableBooks() {
		logger.info("Get all books document by bookStatus = null from Mongo Collection - library ");

		return bookCollectionRepository.findByBookStatus(false);
	}

	public Optional<List<Book>> getBooksByAuthor(String bookAuthor) {
		logger.info("Get libraryMember document by address(city) from Mongo Collection - libraryMember ");
		return bookCollectionRepository.findByBookAuthor(bookAuthor);
	}

	/*
	 * get the library member : by memberName
	 */
	public Optional<LibraryMember> getLibraryMemberByName(String memberName) {
		logger.info("Get libraryMember document by name from Mongo Collection - libraryMember ");
		return libraryMemberEnquiryRepository.findByMemberName(memberName);

	}

	/*
	 * get the library member: by city
	 */
	public List<LibraryMember> getLibraryMembersByCity(String city) {
		logger.info("Get libraryMember document by address(city) from Mongo Collection - libraryMember ");
		return libraryMemberEnquiryRepository.findByCity(city);
	}

	public Optional<LibraryMember> getLibraryMemberById(String memberId) {
		logger.info("Get libraryMember document by memberId from Mongo Collection - libraryMember ");
		return libraryMemberEnquiryRepository.findById(memberId);
	}

}
