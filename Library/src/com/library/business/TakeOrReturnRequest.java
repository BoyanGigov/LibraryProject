package com.library.business;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TakeOrReturnRequest {

	private String username;
	private String isbn;
	private int copies;
	private boolean takeRequest;
	private DateTime deadline;

	protected TakeOrReturnRequest(String username, String isbn, int copies, boolean takeRequest, DateTime deadline) {
		this.username = username;
		this.isbn = isbn;
		this.copies = copies;
		this.takeRequest = takeRequest;
		this.deadline = deadline;
		System.out.println("\nRequest sent. Contact an administrator to complete your request.");
	}

	protected void printRequest(int index) {
		String requestType;
		if (this.takeRequest) {
			requestType = "Take books - ";
		} else {
			requestType = "Return books - ";
		}
		System.out.println("\n" + index + ":\t" + requestType + this.username + ": " + this.isbn + ", copies: "
				+ this.copies + " until " + this.deadline.toString(DateTimeFormat.mediumDate()));
	}

	protected String getUsername() {
		return this.username;
	}

	protected String getIsbn() {
		return this.isbn;
	}

	protected int getCopies() {
		return this.copies;
	}

	protected boolean isTakeRequest() {
		return this.takeRequest;
	}

	protected DateTime getDeadline() {
		return this.deadline;
	}
}
