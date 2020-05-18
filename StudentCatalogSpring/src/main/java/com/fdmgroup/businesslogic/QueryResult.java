package com.fdmgroup.businesslogic;

public class QueryResult {

	private String firstname;
	private String lastname;
	private String ssn;

	public QueryResult(String firstname, String lastname, String ssn) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.ssn = ssn;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getSSN() {
		return ssn;
	}
}
