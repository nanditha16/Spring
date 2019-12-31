package com.example.ajax.api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;


public class Book {
	private int bookId;
	private String bookName;
	private String bookAuthor;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate purchasedDate;
	
	public Book() {
		super();
	}

	public Book(int bookId, String bookName, String bookAuthor, LocalDate purchasedDate) {
		super();
		this.bookId = bookId;
		this.bookName = bookName;
		this.bookAuthor = bookAuthor;
		this.purchasedDate = purchasedDate;
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

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", bookName=" + bookName + ", bookAuthor=" + bookAuthor + ", purchasedDate="
				+ purchasedDate + "]";
	}
	
	
	
	
	
}
