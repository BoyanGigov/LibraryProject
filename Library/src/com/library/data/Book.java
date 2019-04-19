package com.library.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

@XmlRootElement(name = "book")
@XmlType(propOrder = { "isbn", "title", "dateAsString", "authorName", "description", "copiesTotal", "copiesAvailable", "price"})
public class Book {

	@XmlAttribute
	private String isbn;
	@XmlAttribute
	private String title;
	@XmlAttribute
	private String authorName;
	@XmlElement
	private String description;
	private DateTime dateWritten;
	@XmlAttribute
	private int copiesTotal;
	@XmlAttribute
	private int copiesAvailable;
	@XmlAttribute
	private double price;
	@XmlElement(name = "dateWrittenCONVERTED")
	private String dateAsString;
	
// default constructor for XML
	public Book() {
		
	}

	protected Book(String isbn, String title, String authorName, String description, DateTime dateWritten, int copiesTotal,
			double price) {
		this.setIsbn(isbn);
		this.setTitle(title);
		this.setAuthorName(authorName);
		this.setDescription(description);
		this.setDate(dateWritten);
		this.changeCopiesTotal(copiesTotal);
		this.setPrice(price);
		this.dateAsString = DateStringConverter.dateToString(this.dateWritten);
	}

	// Title setter and getter
	private void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	// authorName setter and getter
	protected void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorName() {
		return this.authorName;
	}

	// Description setter and getter
	protected void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	// setDate method
	protected void setDate(DateTime date) {
		this.dateWritten = date;
	}

	public DateTime getDate() {
		return this.dateWritten;
	}

	// price setter and getter
	protected void setPrice(double price) throws IllegalArgumentException {
		if (price > 0) {
			this.price = price;
		} else {
			throw new IllegalArgumentException("Book price not a positive number");
		}
	}

	public double getPrice() {
		return this.price;
	}

	// isbn setter and getter
	private void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIsbn() {
		return this.isbn;
	}

	// copiesTotal modifier and getter
	protected void changeCopiesTotal(int copies) {
			this.copiesTotal += copies;
			this.copiesAvailable += copies;
	}

	public int getCopiesTotal() {
		return copiesTotal;
	}

	// copiesTotal setter and getter
	protected void changeCopiesAvailable(int copies) {
		this.copiesAvailable += copies;
	}

	public int getCopiesAvailable() {
		return copiesAvailable;
	}
	
	// DateString getter
	protected String getDateString() {
		return this.dateAsString;
	}
}
