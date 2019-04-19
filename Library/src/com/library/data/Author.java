package com.library.data;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

@XmlRootElement(name = "author")
@XmlType(propOrder = { "name", "description", "dateAsString", "booksWritten" })
public class Author {

	@XmlAttribute
	private String name;
	private DateTime birthDate;
	@XmlElementWrapper(name = "booksWrittenByAuthor")
	@XmlElement(name = "book")
	private LinkedList<Book> booksWritten = new LinkedList<Book>();
	@XmlElement
	private String description;
	@XmlElement(name = "birthDateCONVERTED")
	private String dateAsString;
	
// default constructor for XML
	public Author() {
		
	}

	protected Author(String name, String description, DateTime birthDate) throws IllegalArgumentException {
		this.setName(name);
		this.setBirthDate(birthDate);
		this.setDescription(description);
		this.dateAsString = DateStringConverter.dateToString(this.birthDate);
	}
	
	// Name setter and getter
	protected void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	// birthDate setter and getter
	protected void setBirthDate(DateTime birthDate) {
		this.birthDate = birthDate;
	}

	public DateTime getBirthDate() {
		return this.birthDate;
	}
	
	// booksWritten setter, getter and modifier
	protected void addBooksWritten(Book tempBook) {
			this.booksWritten.add(tempBook);
	}
	
	protected void removeBooksWritten(Book tempBook) {
		this.booksWritten.remove(tempBook);
	}
	
	public LinkedList<Book> getBooksWritten() {
		return this.booksWritten;
	}

	// description setter and getter
	protected void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	// DateString getter
	public String getDateString() {
		return this.dateAsString;
	}

}
