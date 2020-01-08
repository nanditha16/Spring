package com.example.ajax.api.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "library")
public class Book {

	@Id
	private String id;

	private int bookId;
	private String bookName;
	private String bookAuthor;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate purchasedDate;
	private String bookStatus;

	public Book() {
		super();
	}

	public Book(int bookId, String bookName, String bookAuthor, LocalDate purchasedDate, String bookStatus) {
		super();
		this.bookId = bookId;
		this.bookName = bookName;
		this.bookAuthor = bookAuthor;
		this.purchasedDate = purchasedDate;
		this.bookStatus = bookStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public LocalDate getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(LocalDate purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", bookId=" + bookId + ", bookName=" + bookName + ", bookAuthor=" + bookAuthor
				+ ", purchasedDate=" + purchasedDate + ", bookStatus=" + bookStatus + "]";
	}

}
