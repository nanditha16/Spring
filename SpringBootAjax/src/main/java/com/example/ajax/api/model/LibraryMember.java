package com.example.ajax.api.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "libraryMember")
public class LibraryMember {
	
	@Id
	private String memberId;
	
	private String memberName;
	private String gender;
	
	// one to many relation
	private List<Book> books;
	
	// one to one relation
	private Address address;
	
	public LibraryMember() {
		super();
	}

	public LibraryMember(String memberId, String memberName, String gender, List<Book> books, Address address) {
		super();
		this.memberId = memberId;
		this.memberName = memberName;
		this.gender = gender;
		this.books = books;
		this.address = address;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "LibraryMember [memberId=" + memberId + ", memberName=" + memberName + ", gender=" + gender + ", books="
				+ books + ", address=" + address + "]";
	}


	
}
