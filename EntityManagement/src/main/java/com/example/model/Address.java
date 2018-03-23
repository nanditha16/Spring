package com.example.model;

public class Address implements EMSEntity {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 9014969445211178037L;
	
	private String addressLineOne;
	 private String addressLineTwo;
	 private String city;
	 private String country;
	  
	public Address( final String addressLineOne,
		      final String addressLineTwo,
		      final String city,
		      final String country) {
		this.addressLineOne = addressLineOne;
	    this.addressLineTwo = addressLineTwo;
	    this.city = city;
	    this.country = country;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Override
	public String toString() {
		return "Address{" +
	            "addressLineOne=" + addressLineOne +
	            ", addressLineTwo='" + addressLineTwo + '\'' +
	            ", city='" + city + '\'' +
	            ", country= '" + country + '\'' +
	            '}';
	}

}
